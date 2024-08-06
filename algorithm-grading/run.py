import os
import subprocess
import resource
import ctypes
import signal
import shutil
from typing import List
from pty import STDERR_FILENO, STDIN_FILENO, STDOUT_FILENO
from stat import S_IRUSR, S_IWUSR

libc = ctypes.CDLL('/lib/x86_64-linux-gnu/libc.so.6')
ptrace = libc.ptrace
ptrace.argtypes = [ctypes.c_int, ctypes.c_int,
                   ctypes.c_void_p, ctypes.c_void_p]
ptrace.restype = ctypes.c_int

class user_regs_struct(ctypes.Structure):
    _fields_ = [
        ("r15", ctypes.c_ulonglong),
        ("r14", ctypes.c_ulonglong),
        ("r13", ctypes.c_ulonglong),
        ("r12", ctypes.c_ulonglong),
        ("rbp", ctypes.c_ulonglong),
        ("rbx", ctypes.c_ulonglong),
        ("r11", ctypes.c_ulonglong),
        ("r10", ctypes.c_ulonglong),
        ("r9", ctypes.c_ulonglong),
        ("r8", ctypes.c_ulonglong),
        ("rax", ctypes.c_ulonglong),
        ("rcx", ctypes.c_ulonglong),
        ("rdx", ctypes.c_ulonglong),
        ("rsi", ctypes.c_ulonglong),
        ("rdi", ctypes.c_ulonglong),
        ("orig_rax", ctypes.c_ulonglong),
        ("rip", ctypes.c_ulonglong),
        ("cs", ctypes.c_ulonglong),
        ("eflags", ctypes.c_ulonglong),
        ("rsp", ctypes.c_ulonglong),
        ("ss", ctypes.c_ulonglong),
        ("fs_base", ctypes.c_ulonglong),
        ("gs_base", ctypes.c_ulonglong),
        ("ds", ctypes.c_ulonglong),
        ("es", ctypes.c_ulonglong),
        ("fs", ctypes.c_ulonglong),
        ("gs", ctypes.c_ulonglong),
    ]

def get_systemcall_name() -> List[str] :
    syscallpath = '/usr/include/x86_64-linux-gnu/asm/unistd_64.h'

    arr = []
    with open(syscallpath, 'r') as f :
        lines = f.readlines()
        for line in lines :
            if line.startswith('#define __NR_') :
                syscallnum = int(line.split(' ')[2])
                syscallname = line.split(' ')[1][5:]

                while len(arr) < syscallnum :
                    arr.append('unknown')

                arr.append(syscallname)

    return arr

def print_exit(source_path: str, message: dict) :
    if os.path.exists(source_path):
        for filename in os.listdir(source_path):
            file_path = os.path.join(source_path, filename)
            if os.path.isfile(file_path) or os.path.islink(file_path):
                os.unlink(file_path)
            elif os.path.isdir(file_path):
                shutil.rmtree(file_path)

    return message

def make_commend(language: str) :
    file_name = None
    compile_commend = None
    run_commend = None

    if language == "CPP" :
        file_name = "Solution.cpp"
        compile_commend = f"g++ {file_name} -o Solution"
        run_commend = "./Solution"
    elif language == "JAVA" :
        file_name = "Solution.java"
        compile_commend = f"javac {file_name}"
        run_commend = "java Solution"
    else :
        file_name = "Solution.py" 
        compile_commend = f"python3 -m py_compile {file_name}"
        run_commend = f"python3 {file_name}"

    return file_name, compile_commend, run_commend

def make_file(
    sourece_path: str,
    file_name: str, 
    source_code: str
) : 
    with open(f"{sourece_path}/{file_name}", "w") as f :
        f.writelines(source_code)
        
    return os.path.getsize(f"{sourece_path}/{file_name}")

def compile(execute_path: str, compile_command: str) :
    try :
        compile_process = subprocess.Popen(
            compile_command, shell=True, cwd=execute_path, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
        compile_process.wait(30)
        return compile_process.returncode == 0
    except :
        return False
    
def set_limits(time_limit: int, memory_limit: int) :
    time_limit = (time_limit + 999) // 1000
    resource.setrlimit(resource.RLIMIT_CPU, (time_limit, time_limit + 1))

    # memory_limit = memory_limit * 1024 * 1024
    # resource.setrlimit(resource.RLIMIT_AS, (memory_limit, memory_limit))

    output_limit = 1024 * 1024 * 1024
    resource.setrlimit(resource.RLIMIT_FSIZE, (output_limit, output_limit))
    
def get_memory_usage(pid: int) -> int:
    total = 0
    with open('/proc/{}/maps'.format(pid), 'r') as f :
        for line in f.readlines() :
            sline = line.split()
            start, end = sline[0].split('-')

            start = int(start, 16)
            end = int(end, 16)

            total += end - start
    
    return total // 1024

def compare_output(
        testcase_output_path: str,
        user_output_path: str
) :
    with open(testcase_output_path, 'r') as answer, open(user_output_path, 'r') as output :
        output_lines = [line.strip() + '\n' for line in output.read().strip().splitlines()]
        answer_lines = [line.strip() + '\n' for line in answer.read().strip().splitlines()]

        if len(output_lines) != len(answer_lines) : 
            return False
        
        for output_line, answer_line in zip(output_lines, answer_lines) :
            if output_line != answer_line :
                return False

        return True

def run_testcase(
    execute_path: str,
    execute_command: str,
    testcase_input_path: str,
    testcase_output_path: str,
    user_output_path: str,
    time_limit: int,
    memory_limit: int,
    systemcall_names: List[str]
) :
    result = None

    child = os.fork()
    if child == 0 :
        fdin = os.open(testcase_input_path, os.O_RDONLY)
        os.dup2(fdin, STDIN_FILENO)
        os.close(fdin)

        fdout = os.open(user_output_path, os.O_RDWR | os.O_CREAT, S_IRUSR | S_IWUSR)
        os.dup2(fdout, STDOUT_FILENO)
        os.close(fdout)

        fderr = os.open(os.devnull, os.O_RDWR, S_IRUSR | S_IWUSR)
        os.dup2(fderr, STDERR_FILENO)
        os.close(fderr)

        set_limits(time_limit, memory_limit)
        
        ptrace(0, 0, None, None)

        os.chdir(execute_path)
        execute_command = execute_command.split(' ')
        os.execvp(execute_command[0], execute_command)
    else :
        status: int = 1
        rusage = None
        memory: int = 0

        memory_syscall = ['mmap', 'brk', 'munmap', 'mremap', 'execve']

        while True :
            _, _status, _rusage = os.wait4(child, 0)
            status = _status
            rusage = _rusage

            if os.WIFEXITED(status) :
                break

            if os.WIFSTOPPED(status) :
                if os.WSTOPSIG(status) == signal.SIGXCPU :
                    result = "TIME_ACCESS"
                    os.kill(child, signal.SIGXCPU)
                    break
                if os.WSTOPSIG(status) == signal.SIGSEGV :
                    result = "MEMORY_ACCESS"
                    os.kill(child, signal.SIGXFSZ)
                    break

            if os.WIFSIGNALED(status) :
                result = "RUNTIME_ERROR"
                break

            regs = user_regs_struct()
            ptrace(12, child, None, ctypes.byref(regs))

            if systemcall_names[regs.orig_rax] in memory_syscall :
                now_memory = get_memory_usage(child)
                memory = max(memory, now_memory)


            ptrace(24, child, None, None)

        exitcode = os.WEXITSTATUS(status)

        if exitcode == 0 :
            if compare_output(testcase_output_path, user_output_path) :
                result = "CORRECT"
            else :
                result = "INCORRECT"
        else :
            if result == None :
                result = "RUNTIME_ERROR"

        time = int((rusage[0] + rusage[1]) * 1000)

        if time > time_limit : 
            result = "TIME_ACCESS"

        return result, memory, time

def solve(
    problem_id: int,
    language: str,
    source_code: str,
    time_limit: int, 
    memory_limit: int
) :
    
    testcase_path = f'./testcase/{problem_id}'
    source_path = './source'
    code_filename, compile_commend, run_commend = make_commend(language)

    code_len = make_file(source_path, code_filename, source_code)

    lst = os.listdir(testcase_path)
    testcase_input_paths = []
    testcase_output_paths = []

    for name in lst:
        if name.endswith('.in') :
            testcase_input_paths.append(os.path.join(testcase_path, name))
        elif name.endswith('.out') :
            testcase_output_paths.append(os.path.join(testcase_path, name))
    
    testcase_input_paths.sort()
    testcase_output_paths.sort()

    compile_result = compile(source_path, compile_commend)

    if not compile_result :
        return print_exit(source_path, {"result": "COMPILER_ERROR", 'memory': 0, 'runtime': 0, 'codeLen': code_len})

    output_path = os.path.join(source_path, 'Solution.out')
    systemcall_names = get_systemcall_name()

    max_time = 0
    max_memory = 0

    for testcase_input_path, testcase_output_path in zip(testcase_input_paths, testcase_output_paths) :
        testcase_result, testcase_memory, testcase_time = run_testcase(
            source_path,
            run_commend,
            testcase_input_path,
            testcase_output_path,
            output_path,
            time_limit,
            memory_limit,
            systemcall_names
        )

        os.remove(output_path)

        if testcase_result != "CORRECT" :
            return print_exit(source_path, {"result": testcase_result, 'memory': max_memory, 'runtime': max_time, 'codeLen': code_len})

        max_memory = max(max_memory, testcase_memory)
        max_time = max(max_time, testcase_time)

    return print_exit(source_path, {"result": "CORRECT", "memory": max_memory, "runtime": max_time, 'codeLen': code_len})
