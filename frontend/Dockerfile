# Use the official Node.js image as the base image
FROM node:16-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the package.json and package-lock.json (or yarn.lock) to the working directory
COPY package.json ./
COPY yarn.lock ./

# Install dependencies
RUN yarn install

# Copy the rest of the frontend app files to the working directory
COPY . .

# Build the frontend app using Vite
RUN yarn build

# Expose the port on which the frontend app will run (adjust if needed)
EXPOSE 4173

# Command to run the frontend app using Vite's production server (adjust if needed)
CMD ["yarn", "preview"]