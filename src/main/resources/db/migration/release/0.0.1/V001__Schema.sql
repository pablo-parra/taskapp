CREATE TABLE IF NOT EXISTS task (
    id BIGINT AUTO_INCREMENT,
    title VARCHAR(255),
    description VARCHAR(500),
    creationDate TIMESTAMP,
    dueDate TIMESTAMP,
    done BOOLEAN,
    completionDate TIMESTAMP,
    userId BIGINT
);