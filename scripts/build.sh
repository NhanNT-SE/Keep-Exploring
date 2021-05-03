cd ~/.ssh
ssh -tt -i web-key.pem ec2-user@ec2-18-191-159-74.us-east-2.compute.amazonaws.com -o StrictHostKeyChecking=no << EOF 
cd /home/ec2-user/projects/keep-exploring/back-end
git pull
npm install
exit
EOF
