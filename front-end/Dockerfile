FROM node:lts-alpine
# hacer la carpeta 'app' el directorio de trabajo actual
WORKDIR /app

ENV PATH /app/node_modules/.bin:$PATH

# copiar 'package.json' y 'package-lock.json' (si están disponibles)
COPY package*.json ./
# instalar dependencias del proyecto
RUN npm install
RUN npm install -g @vue/cli
# copiar los archivos y carpetas del proyecto al directorio de trabajo actual (es decir, la carpeta 'app')
COPY . .
# construir aplicación para producción minificada
RUN npm run build

EXPOSE 8080
CMD ["npm", "run", "serve"]
