import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {
  MatBottomSheetModule,
  MatButtonModule,
  MatCardModule, MatChipsModule,
  MatDialogModule, MatGridListModule,
  MatIconModule,
  MatInputModule,
  MatListModule,
  MatProgressBarModule, MatProgressSpinnerModule,
  MatToolbarModule
} from '@angular/material';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FlexLayoutModule} from '@angular/flex-layout';
import {TorrentComponent} from './torrent/torrent.component';
import {RouterModule} from '@angular/router';
import {TvShowListComponent} from './tv-show/tv-show-list.component';
import {DownloadTorrentPopupComponent} from './torrent/download-torrent-popup.component';
import {ConfirmationDialogComponent} from './common/confirmation-dialog.component';
import {RestangularModule} from 'ngx-restangular';
import {FormsModule} from '@angular/forms';
import {DownloadsComponent} from './downloads/downloads.component';
import {TorrentListComponent} from './common/torrent-list.component';
import {ClickableImageGridComponent} from './common/clickable-image-grid.component';
import {TvShowComponent} from './tv-show/tv-show.component';
import {AddTvShowPopupComponent} from './tv-show/add-tv-show-popup.component';
import {ThumbnailPickComponent} from './tv-show/thumbnail-pick.component';
import {EditTvShowPopupComponent} from './tv-show/edit-tv-show-popup.component';
import {AddEpisodePopupComponent} from './tv-show/add-episode-popup.component';
import {ErrorComponent} from './error/error.component';

@NgModule({
  declarations: [
    AppComponent,
    TorrentComponent,
    TvShowListComponent,
    DownloadTorrentPopupComponent,
    ConfirmationDialogComponent,
    DownloadsComponent,
    TorrentListComponent,
    ClickableImageGridComponent,
    TvShowComponent,
    AddTvShowPopupComponent,
    ThumbnailPickComponent,
    EditTvShowPopupComponent,
    AddEpisodePopupComponent,
    ErrorComponent
  ],
  imports: [
    RouterModule.forRoot([
      {path: 'torrents', component: TorrentComponent},
      {path: 'downloads', component: DownloadsComponent},
      {path: 'tv-show/:id', component: TvShowComponent},
      {path: 'tv-show/:id/thumbnail', component: ThumbnailPickComponent},
      {path: 'tv-show', component: TvShowListComponent},
      {path: 'error', component: ErrorComponent},
      {path: '', redirectTo: '/tv-show', pathMatch: 'full'}
    ], {scrollPositionRestoration: 'top'}),
    BrowserModule,
    BrowserAnimationsModule,
    FlexLayoutModule,
    FormsModule,
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
    RouterModule,
    MatListModule,
    MatProgressBarModule,
    MatCardModule,
    MatInputModule,
    MatDialogModule,
    MatBottomSheetModule,
    RestangularModule.forRoot(RestangularProvider => RestangularProvider.setBaseUrl('/api/v1')),
    MatGridListModule,
    MatProgressSpinnerModule,
    MatChipsModule
  ],
  entryComponents: [
    ConfirmationDialogComponent,
    DownloadTorrentPopupComponent,
    AddTvShowPopupComponent,
    EditTvShowPopupComponent,
    AddEpisodePopupComponent
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
