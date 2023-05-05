# Purchase data management APIs

## Constraints
1. Use JAVA or kotlin language based on Spring boot framework
2. Configure a persistence-layer using an in-memory DB (h2, etc.) and create a persistence-layer in that persistence-layer.
3. Store and retrieve data
4. Parsing the CSV file provided when starting the application, or converting the contents of the CSV to SQL.
5. Initial loading of user/product/purchase data
6. Writing unit/integration tests for the functionality

## ER Diagram
<img width="789" alt="스크린샷 2023-05-09 오전 11 35 52" src="https://user-images.githubusercontent.com/25389129/236979678-b3e52f96-97d1-4dd6-a563-a24222444f2e.png">

## Project Structure
The Idea of project structure follows `Ports and Adapters pattern`

1. `Adapter.In`: The classes are responsible for handling user's request from outside of application such as WebUI, ConsoleUI, Message Consumer etc
2. `Adapter.Out`: The classes that implements external layer such as database, restful api, message producer etc
3. `Application`: The classes that implements use cases in `Application.Port.In`
4. `Application.Port.In`: The classes are the abstract definition of what the user would like to do in this application
5. `Application.Port.Out`: The classes are the abstract definition of what this application uses external api
6. `Config`: Configuration Classes
7. `Domain`: Business logics
8. `Handler`: Classes that should be applied globally
9. `Infra.Database`: JPA Entities and Repositories

## API Implementation

### Common
1. Consider proper validation when writing APIs (ID existence, data validation when deleting, etc.)

### Product Management CRUD API
1. Ability to view all products / view individual products / create / modify / delete products

### Product purchase API
1. Receive user ID + product ID to save purchase information
2. A user can purchase multiple products, but cannot purchase the same product twice.

### Purchase statistics output API
1. get total monthly purchase amount by user by inputting year value
2. Get total number of purchases and total amount of sales per month by product by inputting the year value.

## API Response
Success
```
{
    "errorMessage": null,
    "processStatus": "FINISHED",
    "result": [
        {
            "productId": 5000101,
            "productName": "Listening 100 Times",
            "price": 4900
        }
        ...
    ]
}
```

Failed
```
{
    "errorMessage": "No handler found for GET /products/aefasef/aseoijfapejf",
    "processStatus": "NOT_FOUND_RESOURCE",
    "result": null
}
```

## DB Console
1. /console
