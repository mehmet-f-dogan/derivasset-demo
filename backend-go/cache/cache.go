package cache

import (
	"github.com/redis/go-redis/v9"
)

var Rdb *redis.Client

func init() {
	Rdb = redis.NewClient(&redis.Options{
		Addr:     "redis-host:6379",
		Password: "",
		DB:       0,
	})
}
