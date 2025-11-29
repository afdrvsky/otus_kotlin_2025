import Keycloak, { KeycloakConfig } from 'keycloak-js';

const keycloakConfig: KeycloakConfig = {
    url: 'http://localhost:8080', // URL вашего Keycloak сервера
    realm: 'otus-marketplace', // название realm
    clientId: 'otus-marketplace-service', // ID вашего клиента в Keycloak
};

export const keycloak = new Keycloak(keycloakConfig);