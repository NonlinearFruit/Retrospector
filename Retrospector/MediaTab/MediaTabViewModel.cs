using Retrospector.DataStorage.Models;
using Retrospector.MediaTab.Interfaces;

namespace Retrospector.MediaTab
{
    public class MediaTabViewModel : IMediaTab
    {
        public int MediaId { get; set; }
        public Media Media { get; set; }
        public string Header { get; set; } = "Media";
    }
}