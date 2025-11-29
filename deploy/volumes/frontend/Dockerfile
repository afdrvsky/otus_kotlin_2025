# Используем официальный образ Node.js
FROM node:18-alpine AS builder

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем package.json и package-lock.json (или yarn.lock)
COPY mkdservice-frontend/package*.json ./

# Устанавливаем зависимости
RUN npm ci

# Копируем исходный код
COPY mkdservice-frontend .

# Собираем проект
RUN npm run build

# Второй этап — легковесный образ для запуска
FROM nginx:alpine

# Копируем конфиг Nginx
COPY deploy/volumes/nginx/nginx.conf /etc/nginx/nginx.conf

# Копируем собранные файлы в директорию Nginx
COPY --from=builder /app/build /usr/share/nginx/html

# Открываем порт 80
EXPOSE 80

# Запускаем Nginx
CMD ["nginx", "-g", "daemon off;"]