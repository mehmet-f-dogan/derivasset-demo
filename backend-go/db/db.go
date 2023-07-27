package db

import (
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
	"mehmetfd.dev/derivasset-demo/oapi"
)

var DB *gorm.DB // Declare a global variable to hold the database connection

func init() {
	dsn := "host=db-go user=dbuser password=dbpassword dbname=library-management-go port=5432 sslmode=disable"
	db, err := gorm.Open(postgres.Open(dsn), &gorm.Config{})
	if err != nil {
		panic("Failed to connect to the database")
	}

	db.AutoMigrate(&oapi.Author{})
	db.AutoMigrate(&oapi.Book{})

	// Assign the database connection to the global variable
	DB = db
}
