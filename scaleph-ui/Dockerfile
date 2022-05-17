FROM nginx:latest

RUN mkdir /dist
COPY ./dist /dist
COPY ./nginx.conf.template /


CMD envsubst '$$SCALEPH_API_URL' < /nginx.conf.template > /etc/nginx/nginx.conf \
	&& cat /etc/nginx/nginx.conf \
	&& nginx -g 'daemon off;'
