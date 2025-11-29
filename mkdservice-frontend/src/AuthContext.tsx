import React, { createContext, useContext, useState, useEffect } from 'react';
import { keycloak } from './keycloak.config';

interface AuthContextType {
    isAuthenticated: boolean;
    token: string | null;
    login: (redirectUri?: string) => void;
    logout: () => void;
    isLoading: boolean; // Добавляем
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
    const [token, setToken] = useState<string | null>(null);
    const [isLoading, setIsLoading] = useState<boolean>(true);

    useEffect(() => {
        keycloak
            .init({ onLoad: 'check-sso',
                checkLoginIframe: false,
                silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html',})
            .then((authenticated) => {
                setIsAuthenticated(authenticated);
                if (authenticated) {
                    setToken(keycloak.token || null);
                    localStorage.setItem('accessToken', keycloak.token || '');
                }
                if (window.location.hash.includes('iss=')) {
                    window.history.replaceState(
                        {},
                        document.title,
                        window.location.pathname + window.location.search
                    );
                }
                setIsLoading(false); // Завершили проверку
            })
            .catch((err) => {
                console.error('Keycloak init error:', err);
                setIsLoading(false); // Даже при ошибке завершаем загрузку
            });
    }, []);

    const login = (redirectUri?: string) => {
        const targetUri = redirectUri ? window.location.origin + redirectUri : window.location.origin;
        keycloak?.login({
            redirectUri: targetUri,
        });
    };

    const logout = () => {
        keycloak?.logout({
            redirectUri: window.location.origin, // после выхода — на главную, оттуда редирект на /login
        });
        setIsAuthenticated(false);
        setToken(null);
        localStorage.removeItem('accessToken');
    };

    return (
        <AuthContext.Provider value={{ isAuthenticated, token, login, logout, isLoading }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};
