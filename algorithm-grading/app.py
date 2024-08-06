import os
import shutil
from flask import Flask, request, Response
from run import solve

app = Flask(__name__)

@app.route('/judge', methods=['POST'])
def judge() :
    data = request.json
    problem_id = data['problem_id']
    language = data['language']
    source_code = data['source_code']
    time_limit = data['time_limit']
    memory_limit = data['memory_limit']

    return solve(problem_id, language, source_code, time_limit, memory_limit)

@app.route('/problem/<problemId>/testcase', methods=['POST'])
def updateTestcase(problemId) :
    directory = f'./testcase/{problemId}'

    if os.path.exists(directory) :
        shutil.rmtree(directory)

    os.makedirs(directory)

    testcases = request.json['testcases']

    for index, testcase in enumerate(testcases, start=1):
        input = testcase.get('input')
        output = testcase.get('output')

        with open(f"./{directory}/{index}.in", "w") as f:
            f.writelines(input)

        with open(f"./{directory}/{index}.out", "w") as f:
            f.writelines(output)

    return Response(status = 201)

@app.route('/problem/<problemId>/testcase', methods=['GET'])
def getTestcase(problemId) :
    directory = f'./testcase/{problemId}'

    testcases = []
    index = 1
    
    while True:
        input_file = f"./{directory}/{index}.in"
        output_file = f"./{directory}/{index}.out"

        if not os.path.exists(input_file) or not os.path.exists(output_file):
            break

        with open(input_file, "r") as f:
            input = f.read()

        with open(output_file, "r") as f:
            output = f.read()

        testcases.append({
            "input": input,
            "output": output
        })

        index += 1

    return {"testcases": testcases}

if __name__ == '__main__':
    app.run(debug=True)
