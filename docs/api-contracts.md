Identity Service
POST /api/auth/register

POST /api/auth/login

GET /api/organizations/{id}

PUT /api/organizations/{id}/verify


Marketplace Service
POST /api/listings

GET /api/listings

GET /api/listings/{id}

PUT /api/listings/{id}

DELETE /api/listings/{id}

POST /api/listings/{id}/activate

Offers

POST /api/listings/{id}/offers

GET /api/listings/{id}/offers

PUT /api/offers/{id}/accept

PUT /api/offers/{id}/reject
Orders
POST /api/orders

GET /api/orders/{id}

GET /api/orders/my
Shipment
POST /api/orders/{id}/shipment

PUT /api/shipments/{id}/assign

PUT /api/shipments/{id}/pickup

PUT /api/shipments/{id}/deliver
Search
GET /api/search

GET /api/search/autocomplete

GET /api/search/categories