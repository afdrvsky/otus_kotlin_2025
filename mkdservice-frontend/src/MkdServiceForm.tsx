import React, { useState } from 'react';
import './MkdServiceForm.css';
import {useAuth} from "./AuthContext";

const MkdServiceForm: React.FC = () => {
    const [apartmentNumber, setApartmentNumber] = useState<string>('');
    const [meterNumber, setMeterNumber] = useState<string>('');
    const [unit, setUnit] = useState<string>('M3');
    const [reading, setReading] = useState<string>('');
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);
    const {logout, token} = useAuth();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError(null);
        setIsLoading(true);

        // Валидация
        if (!apartmentNumber || !meterNumber || !reading) {
            setError('Пожалуйста, заполните все обязательные поля!');
            setIsLoading(false);
            return;
        }

        const numericReading = parseFloat(reading);
        if (isNaN(numericReading) || numericReading < 0) {
            setError('Значение показания должно быть положительным числом!');
            setIsLoading(false);
            return;
        }

        try {
            if (!token) {
                setError('Токен авторизации не найден. Пожалуйста, авторизуйтесь.');
                setIsLoading(false);
                return;
            }

            const payload = {
                meter: {
                    meterId: meterNumber,
                    apartmentId: apartmentNumber,
                    unit: unit,
                    amount: numericReading.toFixed(2), // 2 знака после запятой
                },
            };

            const response = await fetch('http://localhost:8080/v1/meter/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
                body: JSON.stringify(payload),
            });

            let data;
            try {
                data = await response.json();
            } catch (parseError) {
                setError('Ошибка: не удалось обработать ответ сервера');
                setIsLoading(false);
                return;
            }

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                setError(`Ошибка сервера: ${response.status} ${errorData.message || ''}`);
                setIsLoading(false);
                return;
            }

            if (data.result === 'error' && Array.isArray(data.errors) && data.errors.length > 0) {
                setError(`Ошибка: ${data.errors[0].message}`);
                setIsLoading(false);
                return;
            }

            // Успех
            alert('Показания успешно отправлены!');
            setApartmentNumber('');
            setMeterNumber('');
            setReading('');
        } catch (err) {
            setError('Произошла ошибка при отправке данных. Проверьте подключение к сети.');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="mkd-service-container">
            <header className="mkd-header">
                <h1>MKD Service</h1>
                <p>Сервис сбора показаний счётчиков</p>
                <button
                    className="logout-button"
                    onClick={() => logout()}
                >
                    Выйти
                </button>
            </header>

            {error && <div className="error-message">{error}</div>}

            <form className="mkd-form" onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="apartmentNumber">
                        Номер квартиры <span className="required">*</span>
                    </label>
                    <input
                        type="text"
                        id="apartmentNumber"
                        value={apartmentNumber}
                        onChange={(e) => setApartmentNumber(e.target.value)}
                        placeholder="Введите номер квартиры"
                        required
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="meterNumber">
                        Номер счётчика <span className="required">*</span>
                    </label>
                    <input
                        type="text"
                        id="meterNumber"
                        value={meterNumber}
                        onChange={(e) => setMeterNumber(e.target.value)}
                        placeholder="Введите номер счётчика"
                        required
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="unit">Размерность счётчика</label>
                    <select
                        id="unit"
                        value={unit}
                        onChange={(e) => setUnit(e.target.value)}
                    >
                        <option value="M3">м³ (кубометры)</option>
                        <option value="KWH">кВт·ч (электроэнергия)</option>
                        <option value="GCAL">Гкал (тепло)</option>
                    </select>
                </div>

                <div className="form-group">
                    <label htmlFor="reading">
                        Значение показания <span className="required">*</span>
                    </label>
                    <input
                        type="number"
                        id="reading"
                        value={reading}
                        onChange={(e) => setReading(e.target.value)}
                        placeholder="0.00"
                        step="0.01"
                        min="0"
                        required
                    />
                </div>

                <button
                    type="submit"
                    className="submit-button"
                    disabled={isLoading}
                >
                    {isLoading ? 'Отправляется...' : 'Отправить'}
                </button>
            </form>
        </div>
    );
};

export default MkdServiceForm;
