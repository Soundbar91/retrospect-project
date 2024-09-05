CREATE TABLE `user`
(
    `id`        bigint         NOT NULL AUTO_INCREMENT,
    `email`     varchar(100)   NOT NULL,
    `exp`       decimal(10, 2) NOT NULL DEFAULT '0.00',
    `is_active` bit(1)         NOT NULL DEFAULT b'1',
    `password`  varchar(255)   NOT NULL,
    `role`      tinyint        NOT NULL DEFAULT '0',
    `username`  varchar(20)    NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_email` (`email`),
    UNIQUE KEY `UK_username` (`username`),
    CONSTRAINT `user_chk_1` CHECK (`role` between 0 and 4)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `problem`
(
    `id`                  bigint       NOT NULL AUTO_INCREMENT,
    `algorithms`          text         NOT NULL,
    `answer`              int          NOT NULL DEFAULT '0',
    `create_at`           timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `example_inout`       json         NOT NULL,
    `input_explanation`   text         NOT NULL,
    `level`               int          NOT NULL,
    `memory`              int          NOT NULL,
    `modify_at`           timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `output_explanation`  text         NOT NULL,
    `problem_explanation` text         NOT NULL,
    `runtime`             json         NOT NULL,
    `submit`              int          NOT NULL DEFAULT '0',
    `title`               varchar(100) NOT NULL,
    `user_id`             bigint       NOT NULL,
    PRIMARY KEY (`id`),
    KEY                   `FK_user_id` (`user_id`),
    CONSTRAINT `FK_user_id_P` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `problem_chk_1` CHECK (`level` BETWEEN 1 AND 10),
    CONSTRAINT `problem_chk_2` CHECK (`memory` BETWEEN 1 AND 2048)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `result`
(
    `id`          bigint    NOT NULL AUTO_INCREMENT,
    `code`        text      NOT NULL,
    `code_length` int       NOT NULL,
    `grade`       tinyint   NOT NULL,
    `language`    tinyint   NOT NULL,
    `memory`      int       NOT NULL,
    `runtime`     int       NOT NULL,
    `submit_at`   timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `problem_id`  bigint    NOT NULL,
    `user_id`     bigint    NOT NULL,
    PRIMARY KEY (`id`),
    KEY           `FK_problem_id` (`problem_id`),
    KEY           `FK_user_id` (`user_id`),
    CONSTRAINT `FK_user_id_R` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `FK_problem_id_R` FOREIGN KEY (`problem_id`) REFERENCES `problem` (`id`),
    CONSTRAINT `result_chk_1` CHECK (`grade` between 0 and 5),
    CONSTRAINT `result_chk_2` CHECK (`language` between 0 and 2)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `post`
(
    `id`         bigint       NOT NULL AUTO_INCREMENT,
    `category`   tinyint      NOT NULL,
    `content`    text         NOT NULL,
    `create_at`  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modify_at`  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `title`      varchar(100) NOT NULL,
    `problem_id` bigint       NOT NULL,
    `user_id`    bigint       NOT NULL,
    PRIMARY KEY (`id`),
    KEY          `FK_problem_id` (`problem_id`),
    KEY          `FK_user_id` (`user_id`),
    CONSTRAINT `FK_user_id_PO` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `FK_problem_id_PO` FOREIGN KEY (`problem_id`) REFERENCES `problem` (`id`),
    CONSTRAINT `post_chk_1` CHECK (`category` between 0 and 2)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `post_like`
(
    `id`      bigint NOT NULL AUTO_INCREMENT,
    `post_id` bigint NOT NULL,
    `user_id` bigint NOT NULL,
    PRIMARY KEY (`id`),
    KEY       `FK_post_id` (`post_id`),
    KEY       `FK_user_id` (`user_id`),
    CONSTRAINT `FK_user_id_PL` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `FK_post_id_PL` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `comment`
(
    `id`        bigint    NOT NULL AUTO_INCREMENT,
    `context`   text      NOT NULL,
    `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modify_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `post_id`   bigint    NOT NULL,
    `user_id`   bigint    NOT NULL,
    PRIMARY KEY (`id`),
    KEY         `FK_post_id` (`post_id`),
    KEY         `FK_user_id` (`user_id`),
    CONSTRAINT `FK_user_id_C` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `FK_post_id_C` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `comment_like`
(
    `id`         bigint NOT NULL AUTO_INCREMENT,
    `comment_id` bigint NOT NULL,
    `user_id`    bigint NOT NULL,
    PRIMARY KEY (`id`),
    KEY          `FK_comment_id` (`comment_id`),
    KEY          `FK_user_id` (`user_id`),
    CONSTRAINT `FK_user_id_CL` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `FK_comment_id_CL` FOREIGN KEY (`comment_id`) REFERENCES `comment` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
