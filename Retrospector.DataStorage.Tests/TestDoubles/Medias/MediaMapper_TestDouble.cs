using Retrospector.Core.Crud.Models;
using Retrospector.DataStorage.Medias.Entities;
using Retrospector.DataStorage.Medias.Interfaces;
using Retrospector.DataStorage.Tests.Utilities;

namespace Retrospector.DataStorage.Tests.TestDoubles.Medias
{
    public class MediaMapper_TestDouble : Mapper_TestDouble<Media, MediaEntity>, IMediaMapper
    { }
}