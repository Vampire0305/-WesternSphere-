Overview
WesternShpere is a powerful developer toolset designed to facilitate the development and management of microservices-based applications. It offers a suite of features for secure authentication, media handling, version control, and automated billing workflows.

Why WesternShpere?
This project aims to streamline complex application architectures by providing essential tools for microservices orchestration, security, and operational automation. The core features include:

Microservices Architecture: Built-in service discovery with Eureka to enable scalable, resilient communication between components.

Secure JWT Authentication: Robust token management for secure user and service access.

Media Management: Cloudinary integration for seamless image and video uploads.

Automated Billing: PDF invoice generation and Razorpay payment processing for efficient financial workflows.

Version Control Support: Command-line options for precise git diff operations.

Comprehensive REST APIs: Manage users, jobs, applications, and subscriptions effortlessly.


To start the Project or JobPortal
run all this commands :
mvn clean install -U -DskipTests
java -jar target/JobPortal-0.0.1-SNAPSHOT.jar -Dloader.main=com.jobportal.JobPortal.EurekaServerApplication --spring.profiles.active=eureka   (call this first)
java -jar target/JobPortal-0.0.1-SNAPSHOT.jar -Dloader.main=com.jobportal.JobPortal.AdminServerApplication --spring.profiles.active=adminService
java -jar target/JobPortal-0.0.1-SNAPSHOT.jar -Dloader.main=com.jobportal.JobPortal.AnalyticsServiceApplication --spring.profiles.active=analyticsService
java -jar target/JobPortal-0.0.1-SNAPSHOT.jar -Dloader.main=com.jobportal.JobPortal.ApplicationServiceApplication --spring.profiles.active=applicationService
java -jar target/JobPortal-0.0.1-SNAPSHOT.jar -Dloader.main=com.jobportal.JobPortal.AuthServiceApplication --spring.profiles.active=authService
java -jar target/JobPortal-0.0.1-SNAPSHOT.jar -Dloader.main=com.jobportal.JobPortal.BillingServiceApplication --spring.profiles.active=billingService
java -jar target/JobPortal-0.0.1-SNAPSHOT.jar -Dloader.main=com.jobportal.JobPortal.JobPostServiceApplication --spring.profiles.active=jobService
java -jar target/JobPortal-0.0.1-SNAPSHOT.jar -Dloader.main=com.jobportal.JobPortal.RecruiterServiceApplication --spring.profiles.active=recruiterService
java -jar target/JobPortal-0.0.1-SNAPSHOT.jar -Dloader.main=com.jobportal.JobPortal.StudentServiceApplication --spring.profiles.active=studentService
java -jar target/JobPortal-0.0.1-SNAPSHOT.jar -Dloader.main=com.jobportal.JobPortal.SubscriptionPlanServiceApplication --spring.profiles.active=subscriptionPlanService
java -jar target/JobPortal-0.0.1-SNAPSHOT.jar -Dloader.main=com.jobportal.JobPortal.ApiGateWayApplication --spring.profiles.active=apigateway   (call this last)

or if using Intellij go to run>edit configurations > + >  spring boot > select the main application and add its profile

then ***********************

This is the Postman Export with some functions in it and the rest functions u can take it by running the application and going to http://localhost:{port}/swagger-ui/index.html

here u will get all the apis and their paths.
link : https://.postman.co/workspace/My-Workspace~bc362244-3321-443c-8fe9-1ce6749e5f3a/collection/45377113-484d4ff4-7d85-4db5-bc27-a820541a6140?action=share&creator=45377113
else if export then : 
{
	"info": {
		"_postman_id": "484d4ff4-7d85-4db5-bc27-a820541a6140",
		"name": "Zidio",
		"schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json",
		"_exporter_id": "45377113"
	},
	"item": [
		{
			"name": "Zidio Admin",
			"item": [
				{
					"name": "All Summary",
					"request": {
						"auth": {
							"type": "bearer",
							"bearer": [
								{
									"key": "token",
									"value": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJ0eXBlIjoiYWNjZXNzIiwicGF5U3RhdCI6IlBFTkRJTkciLCJ1c2VySWQiOjEsInN1YiI6InZpcmFqcHJhYmh1NDdAc3R1ZGVudC5zZml0LmFjLmluIiwiaWF0IjoxNzUzNjE0NjEyLCJleHAiOjE3NTM3MDEwMTJ9.vsAPVOrvgG6WgLpbI5jbqw2a-t_RLHuf9Vz6GMlc4I8",
									"type": "string"
								}
							]
						},
						"method": "GET",
						"header": [],
						"url": {
							"raw": "http://localhost:8083/api/analystics/summary",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8083",
							"path": [
								"api",
								"analystics",
								"summary"
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "Zidio Auth",
			"item": [
				{
					"name": "Auth Register for student",
					"protocolProfileBehavior": {
						"disabledSystemHeaders": {
							"content-type": true
						}
					},
					"request": {
						"auth": {
							"type": "noauth"
						},
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json",
								"type": "text"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\r\n  \"name\": \"Viraj\",\r\n  \"email\": \"virajprabhu47@gmail.com\",\r\n  \"password\": \"Viraj2005\",\r\n  \"confirmPassword\": \"Viraj2005\",\r\n  \"role\": \"STUDENT\",\r\n  \"studentProfile\": {\r\n    \"name\": \"Viraj\",\r\n    \"email\": \"virajprabhu47@gmail.com\",\r\n    \"phone\": \"9004041406\",\r\n    \"qualification\": \"B.Tech in Computer Engineering\",\r\n    \"resumeURL\": \"https://example.com/resume.pdf\",\r\n    \"profilePictureURL\": \"https://example.com/profile.jpg\",\r\n    \"dateOfBirth\": \"2003-01-15T00:00:00\",\r\n    \"currentLocation\": \"Mumbai\",\r\n    \"preferredJobLocation\": \"Bangalore\",\r\n    \"experienceYears\": 1,\r\n    \"skills\": [\"Java\", \"Spring Boot\", \"SQL\"],\r\n    \"linkedInProfile\": \"https://linkedin.com/in/viraj\",\r\n    \"githubProfile\": \"https://github.com/viraj\",\r\n    \"portfolioURL\": \"https://viraj.dev\",\r\n    \"expectedSalary\": 600000.0,\r\n    \"isAvailableForHire\": true,\r\n    \"bio\": \"Passionate backend developer looking for impactful projects.\"\r\n  }\r\n}"
						},
						"url": {
							"raw": "http://localhost:8761/api/auth/register",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8761",
							"path": [
								"api",
								"auth",
								"register"
							]
						}
					},
					"response": []
				},
				{
					"name": "Auth Register for admin",
					"protocolProfileBehavior": {
						"disabledSystemHeaders": {
							"content-type": true
						}
					},
					"request": {
						"auth": {
							"type": "noauth"
						},
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json",
								"type": "text"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\r\n  \"name\": \"Viraj47\",\r\n  \"email\": \"virajprabhu47@student.sfit.ac.in\",\r\n  \"password\": \"Viraj2005\",\r\n  \"confirmPassword\": \"Viraj2005\",\r\n  \"role\": \"ADMIN\",\r\n  \"adminUserProfile\": {\r\n    \"name\": \"Viraj47\",\r\n    \"email\": \"virajprabhu47@student.sfit.ac.in\",\r\n    \"phone\": \"9004041406\"\r\n  }\r\n}"
						},
						"url": {
							"raw": "http://localhost:8080/api/auth/register",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8080",
							"path": [
								"api",
								"auth",
								"register"
							]
						}
					},
					"response": []
				},
				{
					"name": "Auth Register for recruiter",
					"protocolProfileBehavior": {
						"disabledSystemHeaders": {
							"content-type": true
						}
					},
					"request": {
						"auth": {
							"type": "noauth"
						},
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json",
								"type": "text"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\r\n  \"name\": \"Viraj101\",\r\n  \"email\": \"virajprabhu6.vp@gmail.com\",\r\n  \"password\": \"Viraj2005\",\r\n  \"confirmPassword\": \"Viraj2005\",\r\n  \"role\": \"RECRUITER\",\r\n  \"recruiterProfile\": {\r\n    \"name\": \"Viraj101\",\r\n    \"email\": \"virajprabhu6.vp@gmail.com\",\r\n    \"phone\": \"9004041406\",\r\n    \"companyName\":\"Viraj Works\",\r\n    \"companyDescription\":\"A Tech Company\",\r\n    \"companyWebsite\":\"https://Viraj47.pythonanywhere.com\",\r\n    \"linkedinProfile\":\"Viraj47\",\r\n    \"companySize\":\"STARTUP\",\r\n    \"industry\":\"Tech\",\r\n    \"companyLocation\":\"MUMBAI\",\r\n    \"companyFoundedYear\":\"2020\"\r\n  }\r\n}"
						},
						"url": {
							"raw": "http://localhost:8080/api/auth/register",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8080",
							"path": [
								"api",
								"auth",
								"register"
							]
						}
					},
					"response": []
				},
				{
					"name": "Auth Login",
					"request": {
						"auth": {
							"type": "noauth"
						},
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json",
								"type": "text"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\r\n    \"email\":\"virajprabhu6.vp@gmail.com\",\r\n    \"password\":\"Viraj2005\"\r\n}"
						},
						"url": {
							"raw": "http://localhost:8081/api/auth/login",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8081",
							"path": [
								"api",
								"auth",
								"login"
							]
						}
					},
					"response": []
				},
				{
					"name": "forget password",
					"request": {
						"auth": {
							"type": "noauth"
						},
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json",
								"type": "text"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\r\n\r\n    \"email\":\"virajprabhu47@student.sfit.ac.in\"\r\n\r\n}"
						},
						"url": {
							"raw": "http://localhost:8080/api/auth/forgot-password",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8080",
							"path": [
								"api",
								"auth",
								"forgot-password"
							]
						}
					},
					"response": []
				},
				{
					"name": "reset password",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n    \"token\":\"7cd79357-1ed9-4958-9545-0c5378ad640e\",\r\n    \"newPassword\":\"Viraj1010\",\r\n    \"confirmPassword\":\"Viraj1010\"\r\n\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:8080/api/auth/reset-password",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8080",
							"path": [
								"api",
								"auth",
								"reset-password"
							]
						}
					},
					"response": []
				},
				{
					"name": "get-new-email-token",
					"request": {
						"auth": {
							"type": "bearer",
							"bearer": [
								{
									"key": "token",
									"value": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJ0eXBlIjoiYWNjZXNzIiwidXNlcklkIjoxLCJzdWIiOiJ2aXJhanByYWJodTQ3QHN0dWRlbnQuc2ZpdC5hYy5pbiIsImlhdCI6MTc1MjQyNjkwMCwiZXhwIjoxNzUyNTEzMzAwfQ.0aXF3VQTlgxGqiVWpsEteqpk1ct12Dx9g1dtVVvjGOw",
									"type": "string"
								}
							]
						},
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json",
								"type": "text"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\r\n  \"name\": \"Viraj101\",\r\n  \"email\": \"virajprabhu6.vp@gmail.com\",\r\n  \"password\": \"Viraj2005\",\r\n  \"confirmPassword\": \"Viraj2005\",\r\n  \"role\": \"RECRUITER\",\r\n  \"recruiterProfile\": {\r\n    \"name\": \"Viraj101\",\r\n    \"email\": \"virajprabhu6.vp@gmail.com\",\r\n    \"phone\": \"9004041406\",\r\n    \"companyName\":\"Viraj Works\",\r\n    \"companyDescription\":\"A Tech Company\",\r\n    \"companyWebsite\":\"https://Viraj47.pythonanywhere.com\",\r\n    \"linkedinProfile\":\"Viraj47\",\r\n    \"companySize\":\"STARTUP\",\r\n    \"industry\":\"Tech\",\r\n    \"companyLocation\":\"MUMBAI\",\r\n    \"companyFoundedYear\":\"2020\"\r\n  }\r\n}"
						},
						"url": {
							"raw": "http://localhost:8080/api/auth/get-email-token",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8080",
							"path": [
								"api",
								"auth",
								"get-email-token"
							]
						}
					},
					"response": []
				},
				{
					"name": "verify-email",
					"protocolProfileBehavior": {
						"disableBodyPruning": true
					},
					"request": {
						"auth": {
							"type": "bearer",
							"bearer": [
								{
									"key": "token",
									"value": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJ0eXBlIjoiYWNjZXNzIiwidXNlcklkIjoxLCJzdWIiOiJ2aXJhanByYWJodTQ3QHN0dWRlbnQuc2ZpdC5hYy5pbiIsImlhdCI6MTc1MjQyNjkwMCwiZXhwIjoxNzUyNTEzMzAwfQ.0aXF3VQTlgxGqiVWpsEteqpk1ct12Dx9g1dtVVvjGOw",
									"type": "string"
								}
							]
						},
						"method": "GET",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json",
								"type": "text"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\r\n  \"name\": \"Viraj101\",\r\n  \"email\": \"virajprabhu6.vp@gmail.com\",\r\n  \"password\": \"Viraj2005\",\r\n  \"confirmPassword\": \"Viraj2005\",\r\n  \"role\": \"RECRUITER\",\r\n  \"recruiterProfile\": {\r\n    \"name\": \"Viraj101\",\r\n    \"email\": \"virajprabhu6.vp@gmail.com\",\r\n    \"phone\": \"9004041406\",\r\n    \"companyName\":\"Viraj Works\",\r\n    \"companyDescription\":\"A Tech Company\",\r\n    \"companyWebsite\":\"https://Viraj47.pythonanywhere.com\",\r\n    \"linkedinProfile\":\"Viraj47\",\r\n    \"companySize\":\"STARTUP\",\r\n    \"industry\":\"Tech\",\r\n    \"companyLocation\":\"MUMBAI\",\r\n    \"companyFoundedYear\":\"2020\"\r\n  }\r\n}"
						},
						"url": {
							"raw": "http://localhost:8080/api/auth/verify-email?token=f89f6058-e912-48c1-ac9d-908e0a51b91e",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8080",
							"path": [
								"api",
								"auth",
								"verify-email"
							],
							"query": [
								{
									"key": "token",
									"value": "f89f6058-e912-48c1-ac9d-908e0a51b91e"
								}
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "Zidio Recruiter",
			"item": [
				{
					"name": "create job",
					"request": {
						"auth": {
							"type": "bearer",
							"bearer": [
								{
									"key": "token",
									"value": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUkVDUlVJVEVSIiwidHlwZSI6ImFjY2VzcyIsInVzZXJJZCI6Niwic3ViIjoidmlyYWpwcmFiaHU2LnZwQGdtYWlsLmNvbSIsImlhdCI6MTc1MzU1NTY1MywiZXhwIjoxNzUzNjQyMDUzfQ.IACPdPq52R4CG5wY0JLf14L2iNTjG1z1aUiVFqJS8Zo",
									"type": "string"
								}
							]
						},
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n  \"title\": \"Software Engineer\",\r\n  \"jobDescription\": \"We are looking for a passionate Software Engineer to design, develop, and install software solutions. The successful candidate will be able to build high-quality, innovative and fully performing software in compliance with coding standards and technical design.\",\r\n  \"location\": \"Bengaluru, India\",\r\n  \"employmentType\": \"Full-time\",\r\n  \"minExperience\": 3,\r\n  \"maxExperience\": 6,\r\n  \"minSalary\": 800000.00,\r\n  \"maxSalary\": 1500000.00,\r\n  \"recruiterId\": 1,\r\n  \"recruiterName\": \"Viraj Prabhu\",\r\n  \"applicantIds\": [\r\n  ]\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:8083/api/recruiters/jobs",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8083",
							"path": [
								"api",
								"recruiters",
								"jobs"
							]
						}
					},
					"response": []
				},
				{
					"name": "pay without signature",
					"protocolProfileBehavior": {
						"disabledSystemHeaders": {}
					},
					"request": {
						"auth": {
							"type": "bearer",
							"bearer": [
								{
									"key": "token",
									"value": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUkVDUlVJVEVSIiwidHlwZSI6ImFjY2VzcyIsInVzZXJJZCI6Niwic3ViIjoidmlyYWpwcmFiaHU2LnZwQGdtYWlsLmNvbSIsImlhdCI6MTc1MzYwMzU2NywiZXhwIjoxNzUzNjg5OTY3fQ.0ZPZk7NI3b_iWsHNfQNaARP72s8v8bFOSwuDE8KD7NU",
									"type": "string"
								}
							]
						},
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n  \"entity\": \"event\",\r\n  \"account_id\": \"1\",\r\n  \"event\": \"payment.captured\",\r\n  \"contains\": [\r\n    \"payment\"\r\n  ],\r\n  \"payload\": {\r\n    \"payment\": {\r\n      \"entity\": {\r\n        \"id\": \"pay_Qy26jX9Y1Z2c3D4e\",  // <-- This is the unique payment ID\r\n        \"entity\": \"payment\",\r\n        \"amount\": 1000,\r\n        \"currency\": \"INR\",\r\n        \"status\": \"captured\",\r\n        \"order_id\": \"order_Qy26iy4b0UwTLV\", // <-- This matches the order you created\r\n        \"invoice_id\": null,\r\n        \"international\": false,\r\n        \"method\": \"card\",\r\n        \"amount_refunded\": 0,\r\n        \"refund_status\": null,\r\n        \"captured\": true,\r\n        \"description\": null,\r\n        \"card_id\": \"card_test_123\",\r\n        \"bank\": null,\r\n        \"wallet\": null,\r\n        \"vpa\": null,\r\n        \"email\": \"virajprabhu6.vp@gmail.com\",\r\n        \"contact\": \"+919004041406\",\r\n        \"notes\": [],\r\n        \"fee\": 12,\r\n        \"tax\": 0,\r\n        \"error_code\": null,\r\n        \"error_description\": null,\r\n        \"created_at\": 1753606481\r\n      }\r\n    }\r\n  },\r\n  \"created_at\": 1753606481\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:8083/api/recruiters/pay",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8083",
							"path": [
								"api",
								"recruiters",
								"pay"
							]
						}
					},
					"response": []
				},
				{
					"name": "initiate payment",
					"request": {
						"auth": {
							"type": "bearer",
							"bearer": [
								{
									"key": "token",
									"value": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUkVDUlVJVEVSIiwidHlwZSI6ImFjY2VzcyIsInVzZXJJZCI6Niwic3ViIjoidmlyYWpwcmFiaHU2LnZwQGdtYWlsLmNvbSIsImlhdCI6MTc1MzYwMzU2NywiZXhwIjoxNzUzNjg5OTY3fQ.0ZPZk7NI3b_iWsHNfQNaARP72s8v8bFOSwuDE8KD7NU",
									"type": "string"
								}
							]
						},
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n    \"amount\":1000\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:8083/api/recruiters/initiate-payment/1",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8083",
							"path": [
								"api",
								"recruiters",
								"initiate-payment",
								"1"
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "Zidio Send Email",
			"item": [
				{
					"name": "send email",
					"request": {
						"auth": {
							"type": "bearer",
							"bearer": [
								{
									"key": "token",
									"value": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUkVDUlVJVEVSIiwidHlwZSI6ImFjY2VzcyIsInVzZXJJZCI6Niwic3ViIjoidmlyYWpwcmFiaHU2LnZwQGdtYWlsLmNvbSIsImlhdCI6MTc1MzYxNjE0MywiZXhwIjoxNzUzNzAyNTQzfQ.OrL9tgF6DCjL8snfqNP010aF4zPACfyr4TLokE2bIvs",
									"type": "string"
								}
							]
						},
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json",
								"type": "text"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\r\n   \"to\":\"virajprabhu6.vp@gmail.com\",\r\n   \"subject\":\"Testing message\",\r\n   \"body\":\"hello there\"\r\n}"
						},
						"url": {
							"raw": "http://localhost:8083/api/notify/send",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8083",
							"path": [
								"api",
								"notify",
								"send"
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "Zidio Student",
			"item": [
				{
					"name": "create student",
					"request": {
						"auth": {
							"type": "bearer",
							"bearer": [
								{
									"key": "token",
									"value": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJ0eXBlIjoiYWNjZXNzIiwic3ViIjoidmlyYWpwcmFiaHU0N0BzdHVkZW50LnNmaXQuYWMuaW4iLCJpYXQiOjE3NTIyNDMzMzgsImV4cCI6MTc1MjMyOTczOH0.uR5gxf67I2Bdch3brrqnCXNNun_92mjqttOBv72WgO0",
									"type": "string"
								}
							]
						},
						"method": "POST",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json",
								"type": "text"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\r\n  \"name\": \"Viraj Prabhu\",\r\n  \"email\": \"virajprabhu47@student.sfit.ac.in\",\r\n  \"phone\": \"9004041406\",\r\n  \"qualification\": \"B.Tech in Computer Engineering\",\r\n  \"resumeURL\": \"https://example.com/resume.pdf\",\r\n  \"profilePictureURL\": \"https://example.com/profile.jpg\",\r\n  \"dateOfBirth\": \"2005-12-12T00:00:00\",\r\n  \"currentLocation\": \"Mumbai\",\r\n  \"preferredJobLocation\": \"Mumbai\",\r\n  \"experienceYears\": 2,\r\n  \"skills\": [\"Java\", \"Spring Boot\", \"SQL\"],\r\n  \"linkedInProfile\": \"https://linkedin.com/in/viraj\",\r\n  \"githubProfile\": \"https://github.com/Viraj47\",\r\n  \"portfolioURL\": \"https://viraj.dev\",\r\n  \"expectedSalary\": 6.5,\r\n  \"isAvailableForHire\": true,\r\n  \"isProfileComplete\": true,\r\n  \"lastLoginAt\": \"2024-07-10T15:30:00\",\r\n  \"createdAt\": \"2024-07-01T10:00:00\",\r\n  \"updatedAt\": \"2024-07-11T11:00:00\",\r\n  \"bio\": \"Enthusiastic software engineer with strong backend skills.\",\r\n  \"isActive\": true\r\n}\r\n"
						},
						"url": {
							"raw": "http://localhost:8080/api/students",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8080",
							"path": [
								"api",
								"students"
							]
						}
					},
					"response": []
				},
				{
					"name": "get student by id",
					"protocolProfileBehavior": {
						"disableBodyPruning": true
					},
					"request": {
						"auth": {
							"type": "bearer",
							"bearer": [
								{
									"key": "token",
									"value": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiU1RVREVOVCIsInR5cGUiOiJhY2Nlc3MiLCJ1c2VySWQiOjIsInN1YiI6InZpcmFqcHJhYmh1NDdAZ21haWwuY29tIiwiaWF0IjoxNzUzNDU0NzQ3LCJleHAiOjE3NTM1NDExNDd9.BOpBLGxXngyKkyADB41MnN994Ugo_u3pFrIv9Lia5bs",
									"type": "string"
								}
							]
						},
						"method": "GET",
						"header": [
							{
								"key": "Content-Type",
								"value": "application/json",
								"type": "text"
							}
						],
						"body": {
							"mode": "raw",
							"raw": "{\r\n  \"name\": \"Viraj Prabhu\",\r\n  \"email\": \"virajprabhu47@student.sfit.ac.in\",\r\n  \"phone\": \"9004041406\",\r\n  \"qualification\": \"B.Tech in Computer Engineering\",\r\n  \"resumeURL\": \"https://example.com/resume.pdf\",\r\n  \"profilePictureURL\": \"https://example.com/profile.jpg\",\r\n  \"dateOfBirth\": \"2005-12-12T00:00:00\",\r\n  \"currentLocation\": \"Mumbai\",\r\n  \"preferredJobLocation\": \"Mumbai\",\r\n  \"experienceYears\": 2,\r\n  \"skills\": [\"Java\", \"Spring Boot\", \"SQL\"],\r\n  \"linkedInProfile\": \"https://linkedin.com/in/viraj\",\r\n  \"githubProfile\": \"https://github.com/Viraj47\",\r\n  \"portfolioURL\": \"https://viraj.dev\",\r\n  \"expectedSalary\": 6.5,\r\n  \"isAvailableForHire\": true,\r\n  \"isProfileComplete\": true,\r\n  \"lastLoginAt\": \"2024-07-10T15:30:00\",\r\n  \"createdAt\": \"2024-07-01T10:00:00\",\r\n  \"updatedAt\": \"2024-07-11T11:00:00\",\r\n  \"bio\": \"Enthusiastic software engineer with strong backend skills.\",\r\n  \"isActive\": true\r\n}\r\n"
						},
						"url": {
							"raw": "http://localhost:8761/api/students/1",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8761",
							"path": [
								"api",
								"students",
								"1"
							]
						}
					},
					"response": []
				},
				{
					"name": "New Request",
					"request": {
						"method": "GET",
						"header": []
					},
					"response": []
				},
				{
					"name": "apply job",
					"request": {
						"auth": {
							"type": "bearer",
							"bearer": [
								{
									"key": "token",
									"value": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiU1RVREVOVCIsInR5cGUiOiJhY2Nlc3MiLCJwYXlTdGF0IjoiUEVORElORyIsInVzZXJJZCI6Miwic3ViIjoidmlyYWpwcmFiaHU0N0BnbWFpbC5jb20iLCJpYXQiOjE3NTM2MTI3MDMsImV4cCI6MTc1MzY5OTEwM30.Iis-oQbe2tzZ9WI-bCtt2FTzpU8O5XqY3U-4_0uuvVc",
									"type": "string"
								}
							]
						},
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n  \"studentId\": 1,\r\n  \"jobPostId\": 1,\r\n  \"studentName\": \"Viraj Prabhu\",\r\n  \"studentEmail\": \"virajprabhu47@gmail.com\",\r\n  \"jobTitle\": \"Junior Developer\",\r\n  \"companyName\": \"Tech Innovators Inc.\",\r\n  \"resumeUrl\": \"https://example.com/resumes/alice_johnson_resume.pdf\",\r\n  \"coverLetter\": \"Dear Hiring Manager, I am writing to express my interest in the Junior Developer position...\",\r\n  \"applicationReference\": \"APP-2025-07-27-001\",\r\n  \"applicationScore\": 95,\r\n  \"feedback\": \"Strong technical skills and good fit for the team.\",\r\n  \"interviewNotes\": \"First round interview completed. Candidate performed well in the coding challenge.\",\r\n  \"status\": \"PENDING\"\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:8083/api/applications/apply",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8083",
							"path": [
								"api",
								"applications",
								"apply"
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "Zidio Subscription",
			"item": [
				{
					"name": "download invoice",
					"protocolProfileBehavior": {
						"disableBodyPruning": true
					},
					"request": {
						"auth": {
							"type": "bearer",
							"bearer": [
								{
									"key": "token",
									"value": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUkVDUlVJVEVSIiwidHlwZSI6ImFjY2VzcyIsInVzZXJJZCI6Niwic3ViIjoidmlyYWpwcmFiaHU2LnZwQGdtYWlsLmNvbSIsImlhdCI6MTc1MzYwMzU2NywiZXhwIjoxNzUzNjg5OTY3fQ.0ZPZk7NI3b_iWsHNfQNaARP72s8v8bFOSwuDE8KD7NU",
									"type": "string"
								}
							]
						},
						"method": "GET",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n  \"entity\": \"event\",\r\n  \"account_id\": \"1\",\r\n  \"event\": \"payment.captured\",\r\n  \"contains\": [\r\n    \"payment\"\r\n  ],\r\n  \"payload\": {\r\n    \"payment\": {\r\n      \"entity\": {\r\n        \"id\": \"pay_Qy26jX9Y1Z2c3D4e\",  // <-- This is the unique payment ID\r\n        \"entity\": \"payment\",\r\n        \"amount\": 1000,\r\n        \"currency\": \"INR\",\r\n        \"status\": \"captured\",\r\n        \"order_id\": \"order_Qy26iy4b0UwTLV\", // <-- This matches the order you created\r\n        \"invoice_id\": null,\r\n        \"international\": false,\r\n        \"method\": \"card\",\r\n        \"amount_refunded\": 0,\r\n        \"refund_status\": null,\r\n        \"captured\": true,\r\n        \"description\": null,\r\n        \"card_id\": \"card_test_123\",\r\n        \"bank\": null,\r\n        \"wallet\": null,\r\n        \"vpa\": null,\r\n        \"email\": \"virajprabhu6.vp@gmail.com\",\r\n        \"contact\": \"+919004041406\",\r\n        \"notes\": [],\r\n        \"fee\": 12,\r\n        \"tax\": 0,\r\n        \"error_code\": null,\r\n        \"error_description\": null,\r\n        \"created_at\": 1753606481\r\n      }\r\n    }\r\n  },\r\n  \"created_at\": 1753606481\r\n}"
						},
						"url": {
							"raw": "http://localhost:8083/api/invoice/download/1",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8083",
							"path": [
								"api",
								"invoice",
								"download",
								"1"
							]
						}
					},
					"response": []
				}
			]
		}
	]
}
