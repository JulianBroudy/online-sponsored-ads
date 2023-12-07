# Spring Boot Online Sponsored Ads Module

## Table of Contents
1. [Project Notes](#project-notes)
2. [Considerations for Improvement](#considerations-for-improvement)
3. [Class & Sequence Diagrams](#class-&-sequence-diagrams)

--- 
## Project Notes

- **DTOs**: I've used the same DTO for both incoming requests and outgoing responses for simplicity. In a more advanced stage, creating separate DTOs would be beneficial for clarity and security.
- **Response Data**: Currently, the API returns comprehensive details about Campaigns and Products. Ideally, this should be tailored to return only essential data.
- **Date-Time Handling**: I chose `Instant` for backend simplicity, but `ZonedDateTime` could be considered for better user experience.
- **Testing Focus**: Given the time constraints, I prioritized testing query and API endpoints. Ideally, I'd add comprehensive unit and integration tests for services, mappers, and error-handling.
<br><br>
- **Future Enhancements**:
    - **Caching**: Implementing second-level caching is on my radar to optimize performance.
    - **Custom Validators**: Developing versatile validators for various use-cases.
    - **Error Handling**: Developing custom exceptions is on my to-do list to improve code readability and maintainability.

## Considerations for Improvement

- **Tests**: Exploring what types of tests would add the most value.
- **Request Handling**: Considering whether dedicated request classes would enhance our design.
- **Serializable Entities**: Assessing the need for entities to implement Serializable.
- **Transactional Use**: Reviewing the usage of the `@Transactional` annotations.
- **Service Layer Design**: Thinking about moving services to interfaces for easier mocking.
- **Product Initialization**: Refining the product generation process for diversity.
- **Security and Rate Limiting**: Integrating Spring Security and rate limiting for the API endpoints.

## Class & Sequence Diagrams

### Class Diagram

![Class Diagram.png](Diagrams%2FClass%20Diagram.png)

### AdController Sequence Diagram

![AdController - serveAd.png](Diagrams%2FAdController%20-%20serveAd.png)

### CampaignController Sequence Diagram

![CampaignController - createCampaign.png](Diagrams%2FCampaignController%20-%20createCampaign.png)
