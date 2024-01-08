CREATE EVENT update_change_token_every_1_day
    ON SCHEDULE EVERY 1 DAY
    DO
    BEGIN
        UPDATE user SET number_of_change_tokens = 2 WHERE to_double_token_change_date IS NULL;
        UPDATE user SET number_of_change_tokens = 4 WHERE to_double_token_change_date = now();
    END;

CREATE EVENT update_delete_token_every_month
    ON SCHEDULE EVERY 1 MONTH
    STARTS TIMESTAMP(CURRENT_DATE, '00:00:00')
    DO
    BEGIN
        UPDATE user SET has_delete_token = true where 1;
    END;

CREATE EVENT update_token_of_user_if_request_change_not_accepted
    ON SCHEDULE EVERY 12 HOUR
    DO
    BEGIN
        update user u SET u.to_double_token_change_date = date_add(now(), INTERVAL 1 DAY) where u.id in (
            SELECT r.old_owner_id from task_change_request r
                  where r.status = 'REJECTED'
                   AND NOW()  > date_add(r.date_request, INTERVAL 12 HOUR)
            );
    end;