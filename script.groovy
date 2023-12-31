def buildApp(){
    echo "Building Application ..."
}


def pushImage(){
    echo 'Building Image ...'
    sh "docker build -t 100.102.123.78:8085/test-go:${BUILD_NUMBER} ."

    echo 'Pushing image to docker hosted rerpository on Nexus'

    withCredentials([usernamePassword(credentialsId: 'nexus', passwordVariable: 'PSW', usernameVariable: 'USER')]){
        sh "echo ${PSW} | docker login -u ${USER} --password-stdin 100.102.123.78:8085"
        sh "docker push 100.102.123.78:8085/test-go:${BUILD_NUMBER}"
    }
}

def testApp(){
    echo 'Testing Application ...'
}

def deployApp(){
    echo 'Deploying Application ...'
}

def commitChanges(){
        withCredentials([usernamePassword(credentialsId: 'github', passwordVariable: 'PSW', usernameVariable: 'USER')]) {
        sh 'git config --global user.name "BataraKresn"'
        sh 'git config --global user.email "adimaspangestu02@gmail.com"'
        sh "git remote set-url origin https://${USER}:${PSW}@github.com/BataraKresn/nexus-CI-Pipeline.git" 
        /*  sh "git remote set-url origin git@git@github.com:BataraKresn/go-example.git  */

        sh '''
            #!/bin/bash
            sed -i 's/Version:.*/Version: '"${BUILD_NUMBER}"'/g' index.html
        '''
        
        sh "git add ."
        sh 'git commit -m "updated version"'
        sh "git push origin HEAD:main"
  }
  echo 'Changes committed by jenkins'
}

return this
