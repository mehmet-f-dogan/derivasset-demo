package main

import (
	"log"

	"github.com/gofiber/fiber/v2"
	"github.com/gofiber/fiber/v2/middleware/cors"
	"mehmetfd.dev/derivasset-demo/handler"
	"mehmetfd.dev/derivasset-demo/oapi"
)

func main() {
	app := fiber.New()

	cors := cors.New()

	app.Use(cors)

	oapi.RegisterHandlers(app, handler.GetHandler())

	log.Fatal(app.Listen(":8080"))
}
