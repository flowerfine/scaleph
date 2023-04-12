#FROM node:18 as build
#
#COPY ./ /scaleph
#RUN cd /scaleph/scaleph-ui-react && npm install && npm run build --prod


FROM nginx:latest as release

RUN mkdir /dist
#COPY --from=build  /scaleph/scaleph-ui-react/dist /dist
#COPY --from=build /scaleph/tools/docker/build/scaleph-ui-react/nginx.conf.template /

COPY ./scaleph-ui-react/dist /usr/share/nginx/html
COPY ./tools/docker/build/scaleph-ui-react/nginx.conf.template /

CMD envsubst '$$SCALEPH_API_URL' < /nginx.conf.template > /etc/nginx/nginx.conf \
	&& cat /etc/nginx/nginx.conf \
	&& nginx -g 'daemon off;'
