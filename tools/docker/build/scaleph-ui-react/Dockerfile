FROM nginx:latest as release

RUN mkdir /dist

COPY scaleph-ui-react/dist /dist
COPY tools/docker/build/scaleph-ui-react/nginx.conf.template /

CMD envsubst '$$SCALEPH_API_URL' < /nginx.conf.template > /etc/nginx/nginx.conf \
	&& cat /etc/nginx/nginx.conf \
	&& nginx -g 'daemon off;'

EXPOSE 8096 8096