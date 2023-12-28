CREATE EVENT update_change_token_every_1_day
    ON SCHEDULE EVERY 1 DAY
    DO
    BEGIN
        UPDATE user SET number_of_change_tokens = 2 WHERE to_double_token_chane_date IS NULL;
        UPDATE user SET number_of_change_tokens = 4 WHERE to_double_token_chane_date = now();
    END;

CREATE EVENT update_delete_token_every_month
    ON SCHEDULE EVERY 1 MONTH
    STARTS TIMESTAMP(CURRENT_DATE, '00:00:00')
    DO
    BEGIN
        UPDATE user SET has_delete_token = true where 1;
    END;