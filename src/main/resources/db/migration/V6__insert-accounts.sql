INSERT INTO account (balance, user_id, account_type) VALUES (100, (SELECT id FROM users WHERE document = '12345678912' LIMIT 1), 'USER');
INSERT INTO account (balance, user_id, account_type) VALUES (0, (SELECT id FROM users WHERE document = '12345678912345' LIMIT 1), 'MERCHANT');