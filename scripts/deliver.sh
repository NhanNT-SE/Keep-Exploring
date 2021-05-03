cd ~/.ssh
ssh -tt -i web-key.pem ec2-user@ec2-18-191-159-74.us-east-2.compute.amazonaws.com -o StrictHostKeyChecking=no << EOF 
pm2 stop react-app
pm2 delete react-app
pm2 serve build 5000 --name "react-app" --spa
EOF