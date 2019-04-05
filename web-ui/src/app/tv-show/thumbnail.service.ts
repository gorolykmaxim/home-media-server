import {Injectable} from '@angular/core';
import {Restangular} from 'ngx-restangular';
import {KeyObjectStorageService} from '../infrastructure/key-object-storage.service';
import {ConnectivityService} from '../infrastructure/connectivity.service';

@Injectable({providedIn: 'root'})
export class ThumbnailService {
  private restangular: Restangular;
  private keyObjectStorage: KeyObjectStorageService;
  private isOnline: boolean;
  constructor(restangular: Restangular, keyObjectStorage: KeyObjectStorageService, connectivityService: ConnectivityService) {
    this.restangular = restangular;
    this.keyObjectStorage = keyObjectStorage;
    this.isOnline = true;
    connectivityService.getIsOnlineObservable().subscribe(status => this.isOnline = status);
  }
  async findThumbnailBySearchTerm(searchTerm) {
    let thumbnails;
    if (this.isOnline) {
      thumbnails = await this.restangular.all('thumbnail').getList({searchTerm}).toPromise();
      this.keyObjectStorage.save(`thumbnails/${searchTerm}`, thumbnails);
    } else {
      thumbnails = this.keyObjectStorage.get(`thumbnails/${searchTerm}`);
    }
    return thumbnails;
  }
}
