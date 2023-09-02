-- Insert sample data for the 'users' table (User entity)
INSERT INTO ajax.users (id, email, password, role, first_name, second_name)
VALUES
    (1, 'user1@example.com', 'password1', 'CUSTOME', 'John', 'Doe'),
    (2, 'user2@example.com', 'password2', 'CUSTOME', 'Jane', 'Smith');

-- Insert sample data for the 'mentees' table (Mentees entity)
INSERT INTO ajax.mentees (id, age, user_id)
VALUES
    (1, 25, 1),
    (2, 30, 2);

-- Insert sample data for the 'mentors' table (Mentors entity)
INSERT INTO ajax.mentors (id, description, isOnline, is_offline_in, rating, user_id)
VALUES
    (1, 'Experienced mentor', TRUE, FALSE, 4.5, 1),
    (2, 'Professional mentor', TRUE, TRUE, 5.0, 2);

-- Insert sample data for the 'categories' table (Categories entity)
INSERT INTO ajax.categories (id, name)
VALUES
    (1, 'Programming'),
    (2, 'Mathematics');

-- Insert sample data for the 'mentorToCategories' table (MentorToCategories entity)
INSERT INTO ajax.mentorToCategories (id, price, rating, categories_id, mentor_id)
VALUES
    (1, 50.0, 4.8, 1, 1),
    (2, 40.0, 4.5, 2, 1),
    (3, 60.0, 4.9, 1, 2),
    (4, 55.0, 4.7, 2, 2);