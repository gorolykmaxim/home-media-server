import {Injectable} from '@angular/core';
import {Restangular} from 'ngx-restangular';

@Injectable({providedIn: 'root'})
export class ThumbnailService {
  constructor(private restangular: Restangular) {}
  async findThumbnailBySearchTerm(searchTerm) {
    return await this.restangular.all('thumbnail').getList({searchTerm}).toPromise();;
  }
}
