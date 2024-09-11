

CREATE TABLE notification (
                              id BIGSERIAL PRIMARY KEY,
                              message VARCHAR(255) NOT NULL,
                              timestamp VARCHAR(255) NOT NULL,
                              app_user_id BIGINT NOT NULL,
                              CONSTRAINT fk_notification_app_user
                                  FOREIGN KEY (app_user_id) REFERENCES app_user(id)
                                      ON DELETE CASCADE
);
