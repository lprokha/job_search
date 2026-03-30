INSERT INTO users (name, surname, age, email, password, phone_number, avatar, account_type)
VALUES ('Alice', 'Johnson', 24, 'alice@example.com', '$2y$10$CGbGx0ZI2CMjDyiXvc.Xw.xAHCx77ivPgCYhjE5MCIREiu6PtFX8O', '+996700111111', 'default-avatar.png', 'APPLICANT'),
       ('Bob', 'Smith', 35, 'bob@example.com', '$2y$10$CGbGx0ZI2CMjDyiXvc.Xw.xAHCx77ivPgCYhjE5MCIREiu6PtFX8O', '+996700222222', 'default-avatar.png', 'EMPLOYER');

INSERT INTO categories (name, parent_id)
VALUES ('IT', NULL),
       ('Backend Development', 1),
       ('QA', 1),
       ('Marketing', NULL);

INSERT INTO resumes (applicant_id, name, category_id, salary, is_active, created_date, update_time)
VALUES (1, 'Java Backend Developer', 2, 80000, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (1, 'QA Engineer', 3, 60000, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO vacancies (name, description, category_id, salary, exp_from, exp_to, is_active, author_id, created_date, update_time)
VALUES ('Junior Java Developer', 'Work with Spring Boot and REST API', 2, 70000, 1, 3, TRUE, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Manual QA Engineer', 'Testing web applications', 3, 50000, 1, 2, TRUE, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO responded_applicants (resume_id, vacancy_id, confirmation)
VALUES (1, 1, FALSE);

INSERT INTO contact_types (type)
VALUES ('EMAIL'),
       ('PHONE'),
       ('TELEGRAM');

INSERT INTO contacts_info (type_id, resume_id, contact_value)
VALUES (1, 1, 'alice@example.com'),
       (2, 1, '+996700111111'),
       (3, 2, '@alice_qa');

INSERT INTO education_info (resume_id, institution, program, start_date, end_date, degree)
VALUES (1, 'Kyrgyz State University', 'Computer Science', DATE '2018-09-01', DATE '2022-06-30', 'Bachelor'),
       (2, 'Kyrgyz Technical University', 'Software Testing', DATE '2019-09-01', DATE '2023-06-30', 'Bachelor');

INSERT INTO work_experience_info (resume_id, years, company_name, position, responsibilities)
VALUES (1, 2, 'TechSoft', 'Java Intern', 'Developed REST endpoints and fixed bugs'),
       (2, 1, 'QA Lab', 'QA Intern', 'Manual testing and writing test cases');

INSERT INTO messages (responded_applicant_id, content, timestamp)
VALUES (1, 'Hello, I am interested in this vacancy', CURRENT_TIMESTAMP);