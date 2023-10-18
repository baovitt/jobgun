## /modules

Within the modules folder, the application is divided into four components:
* shared
* backend
* frontend
* website

### Shared:

The `shared` subproject is responsible for all the abstractions shared between the `backend` and `frontend`. As code that is shared between the `frontend` and `backend`, the `shared` subproject is representative of DRY (Don't Repeat Yourself) principles.

The `shared` subproject contains common utilities for the `backend` and `frontend` subprojects, such as a high-performance implementation of a concurrent non-blocking LRU cache.

### Backend:

The `backend` subproject contains the monolithic backend implementation of jobgun. The `backend` is divided into distinct controllers, which will make it easier to divide into microservices down the road.

Additionally, the `backend` subproject exposes a Swagger UI (automatically generated from the typed endpoint specifications) so developers can quickly test new or updated endpoints.

The backend server was selected to be Armeria, a battle tested Java server selected for maturity and existing integrations with ZIO and Tapir.

### Frontend:

The `frontend` subproject contains all frontend code at the page level or finer. In short, the `frontend` subproject contains page and component UI implementations, as well as component logic.

### Website:

The `website` subproject contains the server configuration, the frontend main function, and the routing and global state logic.

In this folder is the frontend server started by running `yarn dev`.