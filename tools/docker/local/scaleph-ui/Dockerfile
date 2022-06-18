FROM node:16 as build

COPY ./ /scaleph
RUN cd /scaleph/scaleph-ui && npm run build --prod


FROM nginx:latest as release

RUN mkdir /dist
COPY --from=build  /scaleph/scaleph-ui/dist /dist
# COPY ./dist /usr/share/nginx/html
COPY --from=build /scaleph/scaleph-ui/nginx.conf.template /

CMD envsubst '$$SCALEPH_API_URL' < /nginx.conf.template > /etc/nginx/nginx.conf \
	&& cat /etc/nginx/nginx.conf \
	&& nginx -g 'daemon off;'
