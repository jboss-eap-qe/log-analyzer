# log-analyzer
(Multiple) logs analyzer

## Configuration of PostgreSQL

Install and setup PostgreSQL.

```
sudo dnf install postgresql postgresql-server
sudo /usr/bin/postgresql-setup --initdb
sudo service postgresql start

# Create `log-analyzer` user with password `log-analyzer`
sudo -u postgres createuser -P log-analyzer
# Create database
sudo -u postgres createdb -O log-analyzer log-analyzer
```

Try to login with following command. If you are getting an error `psql: FATAL: Ident authentication failed for user`
you likely have to change authentication method in `/var/lib/pgsql/data/pg_hba.conf` from `ident` to `md5`.
See [this article](https://stackoverflow.com/questions/2942485/psql-fatal-ident-authentication-failed-for-user-postgres) for explanation.

```
psql -U log-analyzer -h 127.0.0.1 -W
```

Download [JDBC driver](https://jdbc.postgresql.org/download.html) and create module in Wildfly.

```bash
cd $WILDFLY_HOME
mkdir -p modules/system/layers/base/org/postgresql/main
cd modules/system/layers/base/org/postgresql/main
cp $JDBC_DRIVER .
cat << EOF > module.xml
<?xml version='1.0' encoding='UTF-8'?>

<module xmlns="urn:jboss:module:1.1" name="org.postgresql" slot="main">

    <resources>
        <resource-root path="postgresql-42.2.5.jar"/>
    </resources>

    <dependencies>
        <module name="javax.api"/>
        <module name="javax.transaction.api"/>
    </dependencies>
</module>
EOF
```

Register JDBC driver via Wildfly CLI.

```
/subsystem=datasources/jdbc-driver=postgresql:add(driver-name=postgresql,driver-module-name=org.postgresql,driver-class-name=org.postgresql.Driver)
```

Create datasource via Wildfly CLI.

```
/subsystem=datasources/data-source=LogAnalyzerDS:add(connection-url=jdbc:postgresql://localhost:5432/log-analyzer,driver-name=postgresql,user-name=log-analyzer,password=log-analyzer,jndi-name=java:jboss/datasources/LogAnalyzerDS)
```

