
# Docker Compose Common Commands Reference


Since all services including the Spring Boot application, PostgreSQL database,
and Redis are containerized using Docker Compose, managing and troubleshooting
the environment involves standard Docker Compose commands. Below are frequently
used commands for everyday development and operations.
-------------------

## 1. Start Containers

Build images if needed and start all defined services in the foreground:
```bash 
docker compose up --build
```
Run containers in background (detached mode):
```bash 
docker compose up -d
```
## 2. Stop Containers

Stop running containers without removing them:
```bash 
docker compose stop
```
## 3. Stop and Remove Containers, Networks, Volumes
Clean up all containers and resources created by Docker Compose:
```bash 
docker compose down
```
## 4. View Running Containers
Lists all containers started by Compose for this project:
```bash 
docker compose ps
```
## 5. Check Logs
View aggregated logs from all containers:
```bash 
docker compose logs
```
Follow logs in real-time:
```bash 
docker compose logs -f
```
View logs of a specific service only:
```bash 
docker compose logs <service_name>
```
## 6. Execute Commands inside Containers
Run a shell or any command within a running container (useful for debugging):
```bash 
docker compose exec <service_name> /bin/bash
```
Example: open a shell inside the PostgreSQL container to run SQL commands.

## 7. Restart Containers
Restart all or selected services:
```bash 
docker compose restart
docker compose restart <service_name>
```
## 8. Build/Rebuild Images
Manually trigger a rebuild of service images:
```bash 
docker compose build
docker compose build <service_name>
```
## 9. Pull Latest Images
Update service images from remote registries:
```bash 
docker compose pull
```
Additional Tips
---------------
- Always run 'docker compose up' before developing or testing to ensure all services are running smoothly.
- Use 'docker compose down' to reset the environment without leftover containers or networks.
- Check logs regularly to detect runtime errors early.

--------------------
*End of docker compose Documentation*
