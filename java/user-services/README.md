# DB Changes

* Checking Database’s State

    		java -jar target/user-services.jar db status user.yml

* Migrating New Changes

    		java -jar target/user-services.jar db migrate user.yml


# Running The Application

* Run Server

    		java -jar target/user-services.jar server user.yml
