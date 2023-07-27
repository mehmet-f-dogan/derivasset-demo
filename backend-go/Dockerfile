# Use the official Go image as the base image
FROM golang:1.20-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the Go modules files to the working directory
COPY go.mod go.sum ./

# Download and cache Go dependencies
RUN go mod download

# Copy the rest of the application files to the working directory
COPY . .

# Build the Go binary inside the container
RUN go build -o backend-go .

# Expose the port on which the Go application will run
EXPOSE 8080

# Command to run the Go application
CMD ["./backend-go"]
