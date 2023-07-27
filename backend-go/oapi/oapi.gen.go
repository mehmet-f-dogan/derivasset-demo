// Package oapi provides primitives to interact with the openapi HTTP API.

package oapi

import (
	"fmt"

	"github.com/deepmap/oapi-codegen/pkg/runtime"
	"github.com/gofiber/fiber/v2"
)

// Author defines model for Author.
type Author struct {
	Books      []Book `json:"books"`
	ID   	   int64  `json:"id,omitempty" gorm:"primaryKey;autoIncrement"`
	Name       string `json:"name"`
	YearBorn   int32  `json:"yearBorn"`
}

// AuthorOverviewProjection defines model for AuthorOverviewProjection.
type AuthorOverviewProjection struct {
	Id       int64  `json:"id,omitempty"`
	Name     string `json:"name,omitempty"`
	YearBorn int32  `json:"yearBorn,omitempty"`
}

// Book defines model for Book.
type Book struct {
	ID            int64  `json:"id,omitempty" gorm:"primaryKey;autoIncrement"`
	Data          []byte `json:"data"`
	Name          string `json:"name"`
	YearPublished int32  `json:"yearPublished"`
	Author        Author `json:"author" gorm:"foreignKey:AuthorID"`
	AuthorID      int64  `json:"authorID"`
}

// BookOverviewProjection defines model for BookOverviewProjection.
type BookOverviewProjection struct {
	Id            int64  `json:"id,omitempty"`
	Name          string `json:"name,omitempty"`
	YearPublished int32  `json:"yearPublished,omitempty"`
}

// CreateAuthorRequestDTO defines model for CreateAuthorRequestDTO.
type CreateAuthorRequestDTO struct {
	Name     string `json:"name"`
	YearBorn int32  `json:"yearBorn"`
}

// CreateBookRequestDTO defines model for CreateBookRequestDTO.
type CreateBookRequestDTO struct {
	AuthorId      int64  `json:"authorId"`
	Name          string `json:"name"`
	YearPublished int32  `json:"yearPublished"`
}

// CreateAuthorJSONRequestBody defines body for CreateAuthor for application/json ContentType.
type CreateAuthorJSONRequestBody = CreateAuthorRequestDTO

// CreateBookJSONRequestBody defines body for CreateBook for application/json ContentType.
type CreateBookJSONRequestBody = CreateBookRequestDTO

// ServerInterface represents all server handlers.
type ServerInterface interface {

	// (POST /authors)
	CreateAuthor(c *fiber.Ctx) error

	// (GET /authors/{authorId})
	GetAuthorById(c *fiber.Ctx, authorId int64) error

	// (GET /authors/{authorId}/cached)
	GetAuthorByIdCached(c *fiber.Ctx, authorId int64) error

	// (GET /authors/{authorId}/overview)
	GetAuthorOverviewById(c *fiber.Ctx, authorId int64) error

	// (POST /books)
	CreateBook(c *fiber.Ctx) error

	// (GET /books/{bookId})
	GetBookById(c *fiber.Ctx, bookId int64) error

	// (GET /books/{bookId}/cached)
	GetBookByIdCached(c *fiber.Ctx, bookId int64) error

	// (GET /books/{bookId}/overview)
	GetBookOverviewById(c *fiber.Ctx, bookId int64) error
}

// ServerInterfaceWrapper converts contexts to parameters.
type ServerInterfaceWrapper struct {
	Handler ServerInterface
}

type MiddlewareFunc fiber.Handler

// CreateAuthor operation middleware
func (siw *ServerInterfaceWrapper) CreateAuthor(c *fiber.Ctx) error {

	return siw.Handler.CreateAuthor(c)
}

// GetAuthorById operation middleware
func (siw *ServerInterfaceWrapper) GetAuthorById(c *fiber.Ctx) error {

	var err error

	// ------------- Path parameter "authorId" -------------
	var authorId int64

	err = runtime.BindStyledParameter("simple", false, "authorId", c.Params("authorId"), &authorId)
	if err != nil {
		return fiber.NewError(fiber.StatusBadRequest, fmt.Errorf("Invalid format for parameter authorId: %w", err).Error())
	}

	return siw.Handler.GetAuthorById(c, authorId)
}

// GetAuthorByIdCached operation middleware
func (siw *ServerInterfaceWrapper) GetAuthorByIdCached(c *fiber.Ctx) error {

	var err error

	// ------------- Path parameter "authorId" -------------
	var authorId int64

	err = runtime.BindStyledParameter("simple", false, "authorId", c.Params("authorId"), &authorId)
	if err != nil {
		return fiber.NewError(fiber.StatusBadRequest, fmt.Errorf("Invalid format for parameter authorId: %w", err).Error())
	}

	return siw.Handler.GetAuthorByIdCached(c, authorId)
}

// GetAuthorOverviewById operation middleware
func (siw *ServerInterfaceWrapper) GetAuthorOverviewById(c *fiber.Ctx) error {

	var err error

	// ------------- Path parameter "authorId" -------------
	var authorId int64

	err = runtime.BindStyledParameter("simple", false, "authorId", c.Params("authorId"), &authorId)
	if err != nil {
		return fiber.NewError(fiber.StatusBadRequest, fmt.Errorf("Invalid format for parameter authorId: %w", err).Error())
	}

	return siw.Handler.GetAuthorOverviewById(c, authorId)
}

// CreateBook operation middleware
func (siw *ServerInterfaceWrapper) CreateBook(c *fiber.Ctx) error {

	return siw.Handler.CreateBook(c)
}

// GetBookById operation middleware
func (siw *ServerInterfaceWrapper) GetBookById(c *fiber.Ctx) error {

	var err error

	// ------------- Path parameter "bookId" -------------
	var bookId int64

	err = runtime.BindStyledParameter("simple", false, "bookId", c.Params("bookId"), &bookId)
	if err != nil {
		return fiber.NewError(fiber.StatusBadRequest, fmt.Errorf("Invalid format for parameter bookId: %w", err).Error())
	}

	return siw.Handler.GetBookById(c, bookId)
}

// GetBookByIdCached operation middleware
func (siw *ServerInterfaceWrapper) GetBookByIdCached(c *fiber.Ctx) error {

	var err error

	// ------------- Path parameter "bookId" -------------
	var bookId int64

	err = runtime.BindStyledParameter("simple", false, "bookId", c.Params("bookId"), &bookId)
	if err != nil {
		return fiber.NewError(fiber.StatusBadRequest, fmt.Errorf("Invalid format for parameter bookId: %w", err).Error())
	}

	return siw.Handler.GetBookByIdCached(c, bookId)
}

// GetBookOverviewById operation middleware
func (siw *ServerInterfaceWrapper) GetBookOverviewById(c *fiber.Ctx) error {

	var err error

	// ------------- Path parameter "bookId" -------------
	var bookId int64

	err = runtime.BindStyledParameter("simple", false, "bookId", c.Params("bookId"), &bookId)
	if err != nil {
		return fiber.NewError(fiber.StatusBadRequest, fmt.Errorf("Invalid format for parameter bookId: %w", err).Error())
	}

	return siw.Handler.GetBookOverviewById(c, bookId)
}

// FiberServerOptions provides options for the Fiber server.
type FiberServerOptions struct {
	BaseURL     string
	Middlewares []MiddlewareFunc
}

// RegisterHandlers creates http.Handler with routing matching OpenAPI spec.
func RegisterHandlers(router fiber.Router, si ServerInterface) {
	RegisterHandlersWithOptions(router, si, FiberServerOptions{})
}

// RegisterHandlersWithOptions creates http.Handler with additional options
func RegisterHandlersWithOptions(router fiber.Router, si ServerInterface, options FiberServerOptions) {
	wrapper := ServerInterfaceWrapper{
		Handler: si,
	}

	for _, m := range options.Middlewares {
		router.Use(m)
	}

	router.Post(options.BaseURL+"/authors", wrapper.CreateAuthor)

	router.Get(options.BaseURL+"/authors/:authorId", wrapper.GetAuthorById)

	router.Get(options.BaseURL+"/authors/:authorId/cached", wrapper.GetAuthorByIdCached)

	router.Get(options.BaseURL+"/authors/:authorId/overview", wrapper.GetAuthorOverviewById)

	router.Post(options.BaseURL+"/books", wrapper.CreateBook)

	router.Get(options.BaseURL+"/books/:bookId", wrapper.GetBookById)

	router.Get(options.BaseURL+"/books/:bookId/cached", wrapper.GetBookByIdCached)

	router.Get(options.BaseURL+"/books/:bookId/overview", wrapper.GetBookOverviewById)

}
