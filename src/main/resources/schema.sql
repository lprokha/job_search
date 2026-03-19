DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS work_experience_info;
DROP TABLE IF EXISTS education_info;
DROP TABLE IF EXISTS contacts_info;
DROP TABLE IF EXISTS responded_applicants;
DROP TABLE IF EXISTS vacancies;
DROP TABLE IF EXISTS resumes;
DROP TABLE IF EXISTS contact_types;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    surname      VARCHAR(255),
    age          INT,
    email        VARCHAR(255) NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    phone_number VARCHAR(55),
    avatar       VARCHAR(255),
    account_type VARCHAR(50) NOT NULL
);

CREATE TABLE categories
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    name      VARCHAR(255) NOT NULL,
    parent_id INT,
    CONSTRAINT fk_categories_parent
        FOREIGN KEY (parent_id) REFERENCES categories(id)
);

CREATE TABLE resumes
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    applicant_id INT NOT NULL,
    name         VARCHAR(255) NOT NULL,
    category_id  INT,
    salary       DOUBLE,
    is_active    BOOLEAN,
    created_date TIMESTAMP,
    update_time  TIMESTAMP,
    CONSTRAINT fk_resumes_user
        FOREIGN KEY (applicant_id) REFERENCES users(id),
    CONSTRAINT fk_resumes_category
        FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE vacancies
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    description  TEXT,
    category_id  INT,
    salary       DOUBLE,
    exp_from     INT,
    exp_to       INT,
    is_active    BOOLEAN,
    author_id    INT NOT NULL,
    created_date TIMESTAMP,
    update_time  TIMESTAMP,
    CONSTRAINT fk_vacancies_user
        FOREIGN KEY (author_id) REFERENCES users(id),
    CONSTRAINT fk_vacancies_category
        FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE responded_applicants
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    resume_id    INT NOT NULL,
    vacancy_id   INT NOT NULL,
    confirmation BOOLEAN,
    CONSTRAINT fk_responded_resume
        FOREIGN KEY (resume_id) REFERENCES resumes(id),
    CONSTRAINT fk_responded_vacancy
        FOREIGN KEY (vacancy_id) REFERENCES vacancies(id),
    CONSTRAINT uq_resume_vacancy UNIQUE (resume_id, vacancy_id)
);

CREATE TABLE contact_types
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(255) NOT NULL
);

CREATE TABLE contacts_info
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    type_id   INT NOT NULL,
    resume_id INT NOT NULL,
    value     VARCHAR(255) NOT NULL,
    CONSTRAINT fk_contacts_type
        FOREIGN KEY (type_id) REFERENCES contact_types(id),
    CONSTRAINT fk_contacts_resume
        FOREIGN KEY (resume_id) REFERENCES resumes(id)
);

CREATE TABLE education_info
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    resume_id   INT NOT NULL,
    institution VARCHAR(255) NOT NULL,
    program     VARCHAR(255),
    start_date  DATE,
    end_date    DATE,
    degree      VARCHAR(255),
    CONSTRAINT fk_education_resume
        FOREIGN KEY (resume_id) REFERENCES resumes(id)
);

CREATE TABLE work_experience_info
(
    id               INT AUTO_INCREMENT PRIMARY KEY,
    resume_id        INT NOT NULL,
    years            INT,
    company_name     VARCHAR(255),
    position         VARCHAR(255),
    responsibilities TEXT,
    CONSTRAINT fk_work_resume
        FOREIGN KEY (resume_id) REFERENCES resumes(id)
);

CREATE TABLE messages
(
    id                     INT AUTO_INCREMENT PRIMARY KEY,
    responded_applicant_id INT NOT NULL,
    content                TEXT,
    timestamp              TIMESTAMP,
    CONSTRAINT fk_messages_response
        FOREIGN KEY (responded_applicant_id) REFERENCES responded_applicants(id)
);