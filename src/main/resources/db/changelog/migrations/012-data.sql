INSERT INTO users (name, surname, age, email, password, phone_number, avatar, account_type)
VALUES
    ('Alice', 'Johnson', 24, 'alice@example.com', '$2a$10$EBr29Ej0Rt.e3W/xkXx1gu6kv69c8L1Zl1Czv859xOLJVhMVE2XuO', '+996700111111', 'default-avatar.png', 'APPLICANT'),
    ('Bob', 'Smith', 35, 'bob@example.com', '$2a$10$0iHlNh.EXmRY8IMgKKk2O./PdUL8yre0qeix4qaW.VXbs0Qna2GjO', '+996700222222', 'default-avatar.png', 'EMPLOYER');

INSERT INTO categories (name, parent_id) VALUES ('IT', NULL);

INSERT INTO categories (name, parent_id)
VALUES
    ('Backend Development', (SELECT id FROM categories WHERE name = 'IT')),
    ('QA', (SELECT id FROM categories WHERE name = 'IT')),
    ('Marketing', NULL);

INSERT INTO resumes (applicant_id, name, category_id, salary, is_active, created_date, update_time)
VALUES
    (
        (SELECT id FROM users WHERE email = 'alice@example.com'),
        'Java Backend Developer',
        (SELECT id FROM categories WHERE name = 'Backend Development'),
        80000,
        TRUE,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        (SELECT id FROM users WHERE email = 'alice@example.com'),
        'QA Engineer',
        (SELECT id FROM categories WHERE name = 'QA'),
        60000,
        TRUE,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    );

INSERT INTO vacancies (name, description, category_id, salary, exp_from, exp_to, is_active, author_id, created_date, update_time)
VALUES
    (
        'Junior Java Developer',
        'Work with Spring Boot and REST API',
        (SELECT id FROM categories WHERE name = 'Backend Development'),
        70000,
        1,
        3,
        TRUE,
        (SELECT id FROM users WHERE email = 'bob@example.com'),
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Manual QA Engineer',
        'Testing web applications',
        (SELECT id FROM categories WHERE name = 'QA'),
        50000,
        1,
        2,
        TRUE,
        (SELECT id FROM users WHERE email = 'bob@example.com'),
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    );

INSERT INTO responded_applicants (resume_id, vacancy_id, confirmation)
VALUES (
           (SELECT id FROM resumes WHERE name = 'Java Backend Developer'),
           (SELECT id FROM vacancies WHERE name = 'Junior Java Developer'),
           FALSE
       );

INSERT INTO contact_types (type)
VALUES ('EMAIL'), ('PHONE'), ('TELEGRAM');

INSERT INTO contacts_info (type_id, resume_id, contact_value)
VALUES
    (
        (SELECT id FROM contact_types WHERE type = 'EMAIL'),
        (SELECT id FROM resumes WHERE name = 'Java Backend Developer'),
        'alice@example.com'
    ),
    (
        (SELECT id FROM contact_types WHERE type = 'PHONE'),
        (SELECT id FROM resumes WHERE name = 'Java Backend Developer'),
        '+996700111111'
    ),
    (
        (SELECT id FROM contact_types WHERE type = 'TELEGRAM'),
        (SELECT id FROM resumes WHERE name = 'QA Engineer'),
        '@alice_qa'
    );

INSERT INTO education_info (resume_id, institution, program, start_date, end_date, degree)
VALUES
    (
        (SELECT id FROM resumes WHERE name = 'Java Backend Developer'),
        'Kyrgyz State University',
        'Computer Science',
        DATE '2018-09-01',
        DATE '2022-06-30',
        'Bachelor'
    ),
    (
        (SELECT id FROM resumes WHERE name = 'QA Engineer'),
        'Kyrgyz Technical University',
        'Software Testing',
        DATE '2019-09-01',
        DATE '2023-06-30',
        'Bachelor'
    );

INSERT INTO work_experience_info (resume_id, years, company_name, position, responsibilities)
VALUES
    (
        (SELECT id FROM resumes WHERE name = 'Java Backend Developer'),
        2,
        'TechSoft',
        'Java Intern',
        'Developed REST endpoints and fixed bugs'
    ),
    (
        (SELECT id FROM resumes WHERE name = 'QA Engineer'),
        1,
        'QA Lab',
        'QA Intern',
        'Manual testing and writing test cases'
    );

INSERT INTO messages (responded_applicant_id, content, timestamp)
VALUES (
           (SELECT id FROM responded_applicants LIMIT 1),
           'Hello, I am interested in this vacancy',
           CURRENT_TIMESTAMP
       );