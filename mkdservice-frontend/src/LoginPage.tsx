import React from 'react';
import { useAuth } from './AuthContext';
import './LoginPage.css';

const LoginPage: React.FC = () => {
    const { login } = useAuth();

    return (
        <div className="login-container">
            <div className="login-card">
                <h1>Вход в систему</h1>
                <p>Для доступа к сервису необходимо авторизоваться</p>
                <button onClick={() => login('/')} className="login-button">
                    Войти через Keycloak
                </button>
            </div>
        </div>
    );
};


export default LoginPage;
