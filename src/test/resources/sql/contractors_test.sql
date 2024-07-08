CREATE TABLE IF NOT EXISTS countries (
                                     id SERIAL PRIMARY KEY,
                                     name VARCHAR(255) NOT NULL
                                    );

CREATE TABLE IF NOT EXISTS industries (
                                        id SERIAL PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL
                                        );

CREATE TABLE IF NOT EXISTS org_forms (
                                     id SERIAL PRIMARY KEY,
                                     name VARCHAR(255) NOT NULL
                                    );

CREATE TABLE IF NOT EXISTS contractors (
                                        id SERIAL PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL,
                                        full_name VARCHAR(255),
                                        registration_number VARCHAR(50),
                                        tax_number VARCHAR(50),
                                        country_id INT REFERENCES countries(id),
                                        industry_id INT REFERENCES industries(id),
                                        org_form_id INT REFERENCES org_forms(id)
                                        );



INSERT INTO country (id, name, is_active) VALUES
                                              ('UK', 'United Kingdom', TRUE),
                                              ('US', 'United States', TRUE),
                                              ('CN', 'China', TRUE);

INSERT INTO industry (id, name, is_active) VALUES
                                               (1, 'Information Technology', TRUE),
                                               (2, 'Manufacturing', TRUE),
                                               (3, 'Healthcare', TRUE);

INSERT INTO org_form (id, name, is_active) VALUES
                                               (1, 'Limited Liability Company', TRUE),
                                               (2, 'Joint-Stock Company', TRUE),
                                               (3, 'Sole Proprietorship', TRUE);

INSERT INTO contractor (id, name, name_full,
                        inn, ogrn, parent_id,
                        country, industry, org_form,
                        create_date, modify_date, create_user_id,
                        modify_user_id, is_active) VALUES
                                                       ('1', 'TechCorp', 'TechCorp International LLC',
                                                        '1234567890', '1234567890123', NULL, 'UK', 1,
                                                        1, '2024-07-08 00:00:00', '2024-07-08 00:00:00', 'user',
                                                        'user', TRUE),
                                                                                                                                                                         ('2', 'BuildIt', 'BuildIt Global Ltd.', '2345678901', '2345678901234', NULL, 'US', 2, 2, '2024-07-08 00:00:00', '2024-07-08 00:00:00', 'admin', 'admin', TRUE),
                                                                                                                                                                         ('3', 'HealthPlus', 'HealthPlus International', '3456789012', '3456789012345', NULL, 'CN', 3, 3, '2024-07-08 00:00:00', '2024-07-08 00:00:00', 'admin', 'admin', TRUE);
