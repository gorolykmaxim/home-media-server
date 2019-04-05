import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-clickable-image-grid',
  templateUrl: './clickable-image-grid.component.html'
})
export class ClickableImageGridComponent {
  @Input() images: any[];
  @Output() imageClick = new EventEmitter();
}
