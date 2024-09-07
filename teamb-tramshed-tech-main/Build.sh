# build-frontend.sh
#!/bin/bash
npx tailwindcss -i ./src/main/resources/static/css/tailstyle.css -o ./src/main/resources/static/output.css --watch
