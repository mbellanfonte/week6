// Uses Declarative syntax to run commands inside a container.
pipeline {
    agent {
        kubernetes {
            // Rather than inline YAML, in a multibranch Pipeline you could use: yamlFile 'jenkins-pod.yaml'
            // Or, to avoid YAML:
            // containerTemplate {
            //     name 'shell'
            //     image 'ubuntu'
            //     command 'sleep'
            //     args 'infinity'
            // }
            yaml '''
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: shell
    image: ubuntu
    command:
    - sleep
    args:
    - infinity
'''
        }
    }
    stages {
        stage('Debug') {
            steps {
                echo env.GIT_BRANCH
                echo env. GIT_LOCAL_BRANCH
            }
        }
        stage('Main') {
            when {
                // beforeAgent true
                expression {
                    return env.GIT_BRANCH == "origin/main"
                }
            }
            steps {
                sh 'hostname'
            }
        }
        stage('Feature') {
            when {
                // beforeAgent true
                expression {
                    return env.GIT_BRANCH == "origin/feature"
                }
            }
            steps {
                echo "I am a feature branch"
            }
        }
        stage('Playground') {
            when {
                // beforeAgent true
                expression {
                    return env.GIT_BRANCH == "origin/playground"
                }
            }
            steps {
                echo "I am a playground branch"
            }
        }
    }
}
