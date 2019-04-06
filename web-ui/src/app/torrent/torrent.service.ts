import {EventEmitter, Injectable} from '@angular/core';
import {Restangular} from 'ngx-restangular';

@Injectable({providedIn: 'root'})
export class TorrentService {
  private torrentsObservable = new EventEmitter();
  constructor(private restangular: Restangular) {}
  getTorrentsObservable() {
    return this.torrentsObservable;
  }
  async refresh() {
    const torrents = await this.restangular.all('torrent').getList().toPromise();
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
