FROM liquibase/liquibase:4.31.1

# Копирование конфигурации и миграций
COPY ./src/main/liquibase/* /liquibase/

COPY ./src/main/libs/*.jar /liquibase/lib/

CMD ["liquibase", "--changelog-file=changelog.xml", "update"]
#CMD ["/usr/bin/bash", "-c", "sleep 3600"]