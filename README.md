# Transparent Kitchen – Backend

A reactive, multi-module Spring Boot application designed to provide transparency in food services through live kitchen feeds and automated nutritional integrity checks.

## 🏗 Architecture & Design Patterns

### 1. Saga Pattern (Choreography-based)

This project implements the **Saga Pattern** to maintain data consistency across distributed modules (Order, Kitchen, and Nutrition) without using heavy-duty distributed transactions.

* **Flow**: When an order is placed, the system emits an `OrderPlaced` event.
* **Verification**: The Kitchen module listens and validates the order's nutritional content (calories, fiber, and protein).
* **Resolution**: Based on the result, the order status is updated to `COMPLETED` or `REJECTED`.
* **Compensation**: If verification fails, a compensation step triggers to notify the user and update the database with the failure reason.

### 2. Flexible Metadata with PostgreSQL JSONB

To handle dynamic request data and complex order items—like the **Varkey's Pazhakanji Bowl**—we use a PostgreSQL `JSONB` column.

* **Storage**: The `Order` record utilizes the `io.r2dbc.postgresql.codec.Json` type to store full request snapshots.
* **Efficiency**: Using `JSONB` allows for indexing and efficient querying of specific data within the metadata column.

---

## 🛠 Tech Stack

* **Core**: Java 21 (with Java 25 toolchain).
* **Framework**: Spring Boot 3.4.1 (WebFlux).
* **Database**: PostgreSQL / Supabase with R2DBC for non-blocking I/O.
* **Messaging**: Internal `SagaBus` for asynchronous event handling.
* **Build Tool**: Gradle (Multi-module setup).

---

## 🚀 Getting Started

### Prerequisites

* JDK 25 (Toolchain).
* PostgreSQL/Supabase.

### Installation

1. Clone the repository:
```bash
git clone https://github.com/Arjun-K-Varkey/transparent-kitchen.git

```


2. Run the application:
```bash
./gradlew :kitchen-api:bootRun

```
