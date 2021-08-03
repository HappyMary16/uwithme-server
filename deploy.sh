scp -r ./university-with-me-service/target ubuntu@ec2-18-189-141-10.us-east-2.compute.amazonaws.com:/home/ubuntu/educationapp
ssh ubuntu@ec2-18-189-141-10.us-east-2.compute.amazonaws.com sudo service educationapp restart
