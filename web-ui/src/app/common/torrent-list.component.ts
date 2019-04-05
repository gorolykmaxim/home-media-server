import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-torrent-list',
  templateUrl: './torrent-list.component.html'
})
export class TorrentListComponent {
  @Input() torrents: any[];
  @Input() allowRemoval: boolean;
  @Output() delete = new EventEmitter();
  remove(torrent) {
    this.delete.emit(torrent);
  }
}
