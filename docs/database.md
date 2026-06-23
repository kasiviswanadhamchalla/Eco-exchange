identity service:

organizations

id BIGINT PK

name

industry_type

gst_number

verification_status

city

state

country

created_at

------------
users

id BIGINT PK

organization_id

name

email

password_hash

role

status

created_at
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
marketplace service

material_categories

id

name

description
--------
listings

id

seller_org_id

category_id

title

description

quantity

available_quantity

unit

price_per_unit

location

status

created_at
-----------------
listing_images

id

listing_id

image_url
---
quality_certificates

id

listing_id

certificate_name

certificate_url
-------
offers

id

listing_id

buyer_org_id

quantity

offered_price

status

created_at

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
ORder service:

orders

id

listing_id

buyer_org_id

seller_org_id

offer_id

total_amount

status

created_at

------------------
shipments

id

order_id

partner_id

tracking_number

status
~~~~~~~~~~~~~~~~~~~~~
notifications

id

user_id

title

message

status

created_at

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Analytics service

daily_metrics

id

date

waste_reused_tons

co2_saved

revenue

orders
---------------




Mongo DB:

{
  "eventId": "uuid",
  "service": "marketplace-service",
  "action": "LISTING_CREATED",
  "actorId": 123,
  "payload": {},
  "timestamp": "..."
}




