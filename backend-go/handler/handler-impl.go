package handler

import (
	"context"
	"crypto/rand"
	"encoding/json"
	"fmt"
	"math/big"

	"github.com/gofiber/fiber/v2"
	"mehmetfd.dev/derivasset-demo/cache"
	"mehmetfd.dev/derivasset-demo/db"
	"mehmetfd.dev/derivasset-demo/oapi"
)

var ctx = context.Background()

type HandlerImplementation struct {
}

func getAuthor(authorId int64) (*oapi.Author, error) {
	var author oapi.Author

	err := db.DB.Preload("Books").First(&author, authorId).Error

	return &author, err
}

func getAuthorWithoutBooks(authorId int64) (*oapi.Author, error) {
	var author oapi.Author

	err := db.DB.First(&author, authorId).Error

	return &author, err
}

func getBook(bookId int64) (*oapi.Book, error) {
	var book oapi.Book

	err := db.DB.Preload("Author").First(&book, bookId).Error

	return &book, err
}

func getBookWithoutAuthor(bookId int64) (*oapi.Book, error) {
	var book oapi.Book

	err := db.DB.First(&book, bookId).Error

	return &book, err
}

func generateRandomBytes(max_bytes int) ([]byte, error) {
	n, err := rand.Int(rand.Reader, big.NewInt(int64(max_bytes)))
	if err != nil {
		return nil, err
	}

	randomBytes := make([]byte, n.Int64())

	_, err = rand.Read(randomBytes)
	if err != nil {
		return nil, err
	}

	return randomBytes, nil
}

// (POST /authors)
func (h *HandlerImplementation) CreateAuthor(c *fiber.Ctx) error {
	var authorProjection oapi.AuthorOverviewProjection

	err := c.BodyParser(&authorProjection)
	if err != nil {
		c.Status(400)
		return err
	}

	author := oapi.Author{
		Name:     authorProjection.Name,
		YearBorn: authorProjection.YearBorn,
		Books:    []oapi.Book{},
	}

	err = db.DB.Save(&author).Error
	if err != nil {
		c.Status(500)
		return err
	}

	err = c.JSON(author)
	if err != nil {
		c.Status(500)
		return err
	}

	c.Status(200)
	return err
}

// (GET /authors/{authorId})
func (h *HandlerImplementation) GetAuthorById(c *fiber.Ctx, authorId int64) error {

	author, err := getAuthor(authorId)

	if err != nil {
		c.Status(404)
		return err
	}

	err = c.JSON(author)
	if err != nil {
		c.Status(500)
		return err
	}

	c.Status(200)
	return err
}

// (GET /authors/{authorId}/cached)
func (h *HandlerImplementation) GetAuthorByIdCached(c *fiber.Ctx, authorId int64) error {
	val, err := cache.Rdb.Get(ctx, fmt.Sprintf("author-%v", authorId)).Result()
	if err == nil {
		c.Set("Content-Type", "application/json")
		c.SendString(val)
		c.Status(200)
		return err
	}

	author, err := getAuthor(authorId)

	if err != nil {
		c.Status(404)
		return err
	}

	json, err := json.Marshal(author)
	if err != nil {
		c.Status(500)
		return err
	}

	cache.Rdb.Set(ctx, fmt.Sprintf("author-%v", authorId), string(json), 0)
	c.Set("Content-Type", "application/json")
	c.Send(json)
	c.Status(200)
	return err
}

// (GET /authors/{authorId}/overview)
func (h *HandlerImplementation) GetAuthorOverviewById(c *fiber.Ctx, authorId int64) error {
	author, err := getAuthorWithoutBooks(authorId)

	if err != nil {
		c.Status(404)
		return err
	}

	authorProjection := oapi.AuthorOverviewProjection{
		Id:       author.ID,
		Name:     author.Name,
		YearBorn: author.YearBorn,
	}

	err = c.JSON(authorProjection)
	if err != nil {
		c.Status(500)
		return err
	}

	c.Status(200)
	return err
}

// (POST /books)
func (h *HandlerImplementation) CreateBook(c *fiber.Ctx) error {
	var requestBody oapi.CreateBookJSONRequestBody

	err := c.BodyParser(&requestBody)
	if err != nil {
		c.Status(400)
		return err
	}

	bytes, _ := generateRandomBytes(1000000)

	book := oapi.Book{
		AuthorID:      requestBody.AuthorId,
		Data:          bytes,
		Name:          requestBody.Name,
		YearPublished: requestBody.YearPublished,
	}

	err = db.DB.Save(&book).Error
	if err != nil {
		c.Status(500)
		return err
	}

	err = c.JSON(book)
	if err != nil {
		c.Status(500)
		return err
	}

	c.Status(200)
	return err
}

// (GET /books/{bookId})
func (h *HandlerImplementation) GetBookById(c *fiber.Ctx, bookId int64) error {
	book, err := getBook(bookId)

	if err != nil {
		c.Status(404)
		return err
	}

	err = c.JSON(book)
	if err != nil {
		c.Status(500)
		return err
	}

	c.Status(200)
	return err
}

// (GET /books/{bookId}/cached)
func (h *HandlerImplementation) GetBookByIdCached(c *fiber.Ctx, bookId int64) error {
	val, err := cache.Rdb.Get(ctx, fmt.Sprintf("book-%v", bookId)).Result()
	if err == nil {
		c.Set("Content-Type", "application/json")
		c.SendString(val)
		c.Status(200)
		return err
	}

	book, err := getBook(bookId)

	if err != nil {
		c.Status(404)
		return err
	}

	json, err := json.Marshal(book)
	if err != nil {
		c.Status(500)
		return err
	}

	cache.Rdb.Set(ctx, fmt.Sprintf("book-%v", bookId), string(json), 0)
	c.Set("Content-Type", "application/json")
	c.Send(json)
	c.Status(200)
	return err
}

// (GET /books/{bookId}/overview)
func (h *HandlerImplementation) GetBookOverviewById(c *fiber.Ctx, bookId int64) error {
	book, err := getBookWithoutAuthor(bookId)

	if err != nil {
		c.Status(404)
		return err
	}

	bookProjection := oapi.BookOverviewProjection{
		Id:            book.ID,
		Name:          book.Name,
		YearPublished: book.YearPublished,
	}

	err = c.JSON(bookProjection)
	if err != nil {
		c.Status(500)
		return err
	}

	c.Status(200)
	return err
}

var impl *HandlerImplementation

func init() {
	impl = &HandlerImplementation{}
}

func GetHandler() oapi.ServerInterface {
	return impl
}
