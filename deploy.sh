rsync -crlOt ./university-management-service/target/EducationApp.jar ubuntu@ec2-18-189-141-10.us-east-2.compute.amazonaws.com:/home/ubuntu/educationapp/EducationApp.jar
ssh ubuntu@ec2-18-189-141-10.us-east-2.compute.amazonaws.com sudo service educationapp restart
