import {EventEmitter, Injectable} from '@angular/core';
import {Restangular} from 'ngx-restangular';
import {KeyObjectStorageService} from '../infrastructure/key-object-storage.service';
import {ConnectivityService} from '../infrastructure/connectivity.service';

@Injectable({providedIn: 'root'})
export class TorrentService {
  private torrentsObservable = new EventEmitter();
  private isOnline: boolean;
  private keyObjectStorage: KeyObjectStorageService;
  private restangular: Restangular;
  constructor(restangular: Restangular, connectivityService: ConnectivityService, keyObjectStorage: KeyObjectStorageService) {
    this.restangular = restangular;
    this.keyObjectStorage = keyObjectStorage;
    this.isOnline = true;
    connectivityService.getIsOnlineObservable().subscribe(status => this.isOnline = status);
  }
  getTorrentsObservable() {
    return this.torrentsObservable;
  }
  async refresh() {
    let torrents;
    if (this.isOnline) {
      torrents = await this.restangular.all('torrent').getList().toPromise();
      this.keyObjectStorage.save('torrents', torrents);
    } else {
      torrents = this.keyObjectStorage.get('torrents');
    }
    this.torrentsObservable.emit(torrents);
  }
  async addTorrent(torrent) {
    await this.restangular.all('torrent').post(torrent).toPromise();
    await this.refresh();
  }
  async removeTorrent(torrent) {
    await torrent.remove().toPromise();
    await this.refresh();
  }
}
