using Retrospector.Main.Interfaces;

namespace Retrospector.MediaTab.Interfaces
{
    public interface IMediaTab : ITab
    {
        void Load(int mediaId);
        void New();
    }
}